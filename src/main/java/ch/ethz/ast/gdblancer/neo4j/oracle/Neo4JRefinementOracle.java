package ch.ethz.ast.gdblancer.neo4j.oracle;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.cypher.ast.*;
import ch.ethz.ast.gdblancer.common.schema.CypherEntity;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.common.schema.CypherType;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.Neo4JUtil;
import ch.ethz.ast.gdblancer.neo4j.ast.*;
import ch.ethz.ast.gdblancer.util.Randomization;
import org.neo4j.values.storable.DurationValue;
import org.neo4j.values.storable.PointValue;

import java.time.LocalDate;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Neo4JRefinementOracle implements Oracle {

    private final GlobalState<Neo4JConnection> state;
    private final CypherSchema schema;

    public Neo4JRefinementOracle(GlobalState<Neo4JConnection> state, CypherSchema schema) {
        this.state = state;
        this.schema = schema;
    }

    @Override
    public void check() {
        int amount = Randomization.nextInt(2, 5);
        List<String> labels = new ArrayList<>(10);
        CypherExpression whereExpression = new CypherConstant.BooleanConstant(true);

        ExpectedErrors errors = new ExpectedErrors();

        Neo4JUtil.addRegexErrors(errors);
        Neo4JUtil.addArithmeticErrors(errors);
        Neo4JUtil.addFunctionErrors(errors);

        for (int i = 0; i < amount; i++) {
            labels.add(schema.getRandomLabel());
        }

        // Create match query
        StringBuilder query = new StringBuilder("MATCH ");
        String separator = "";

        for (int i = 0; i < amount; i++) {
            String label = labels.get(i);
            CypherEntity entity = schema.getEntityByLabel(label);

            query.append(separator);
            query.append(String.format("(n%d:%s)", i, label));
            separator = ", ";
            CypherExpression expression = Neo4JExpressionGenerator.generateExpression(Map.of(String.format("n%d", i), entity), CypherType.BOOLEAN);
            whereExpression = new CypherBinaryLogicalOperation(whereExpression, expression, CypherBinaryLogicalOperation.BinaryLogicalOperator.AND);
        }

        query.append(" WHERE ");
        query.append(CypherVisitor.asString(whereExpression));
        query.append(" RETURN ");

        separator = "";
        for (int i = 0; i < amount; i++) {
            query.append(separator);
            query.append(String.format("properties(n%d), id(n%d)", i, i));
            separator = ", ";
        }

        List<Map<String, Object>> results = new Neo4JQuery(query.toString(), errors).executeAndGet(state);

        if (results == null) {
            return;
        }

        if (results.isEmpty()) {
            return;
        }

        Map<String, Object> pivotResult = results.get(0);
        List<Long> expectedIds = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            Long id = (Long) pivotResult.get(String.format("id(n%d)", i));
            expectedIds.add(id);
        }

        CypherExpression refinedWhere = new CypherConstant.BooleanConstant(true);

        for (int current = 0; current < amount; current++) {
            if (!verify(labels, expectedIds, refinedWhere)) {
                throw new AssertionError("Not present");
            } else {
                // refine current
                Map<String, Object> properties = (Map<String, Object>) pivotResult.get(String.format("properties(n%d)", current));
                String label = labels.get(current);
                CypherEntity entity = schema.getEntityByLabel(label);
                String key = Randomization.fromSet(entity.getAvailableProperties().keySet());
                CypherType type = entity.getAvailableProperties().get(key);

                Object value = properties.get(key);
                CypherExpression expectedConstant;

                if (value == null) {
                    CypherExpression branch = new CypherPostfixOperation(new CypherVariablePropertyAccess(String.format("n%d.%s", current, key)), CypherPostfixOperation.PostfixOperator.IS_NULL);
                    refinedWhere = new CypherBinaryLogicalOperation(refinedWhere, branch, CypherBinaryLogicalOperation.BinaryLogicalOperator.AND);
                } else {
                    switch (type) {
                        case BOOLEAN:
                            expectedConstant = new CypherConstant.BooleanConstant((Boolean) value);
                            break;
                        case FLOAT:
                            expectedConstant = new CypherConstant.FloatConstant((Double) value);

                            Double refinedFloat = (Double) value;

                            // Handle special cases such as Infinity and NaN which aren't supported in Neo4J directly
                            if (refinedFloat.isInfinite()) {
                                if (refinedFloat > 0) {
                                    expectedConstant = new CypherBinaryArithmeticOperation(new CypherConstant.FloatConstant(1.0D),
                                            new CypherConstant.FloatConstant(0.0D),
                                            CypherBinaryArithmeticOperation.ArithmeticOperator.DIVISION);
                                } else {
                                    expectedConstant = new CypherBinaryArithmeticOperation(new CypherConstant.FloatConstant(-1.0D),
                                            new CypherConstant.FloatConstant(0.0D),
                                            CypherBinaryArithmeticOperation.ArithmeticOperator.DIVISION);
                                }
                            } else if (refinedFloat.isNaN()) {
                                // NaN == NaN is always false
                                // Therefore we use toString(n.p) == "NaN" which should work
                                CypherExpression toString = new CypherFunctionCall(Neo4JFunction.TO_STRING, new CypherVariablePropertyAccess[]{new CypherVariablePropertyAccess(String.format("n%d.%s", current, key))});
                                CypherExpression expectedString = new CypherConstant.StringConstant("NaN");
                                CypherExpression branch = new CypherBinaryComparisonOperation(toString, expectedString, CypherBinaryComparisonOperation.BinaryComparisonOperator.EQUALS);
                                refinedWhere = new CypherBinaryLogicalOperation(refinedWhere, branch, CypherBinaryLogicalOperation.BinaryLogicalOperator.AND);
                                continue;
                            } else {
                                expectedConstant = new CypherConstant.FloatConstant(refinedFloat);
                            }

                            break;
                        case INTEGER:
                            expectedConstant = new CypherConstant.IntegerConstant((Long) value);
                            break;
                        case STRING:
                            expectedConstant = new CypherConstant.StringConstant((String) value);
                            break;
                        case DATE:
                            LocalDate refinedDate = (LocalDate) value;
                            expectedConstant = new Neo4JDateConstant(false, refinedDate.getYear(), refinedDate.getMonthValue(), refinedDate.getDayOfMonth());
                            break;
                        case LOCAL_TIME:
                            OffsetTime refinedTime = (OffsetTime) value;
                            expectedConstant = new Neo4JLocalTimeConstant(refinedTime.getHour(), "", refinedTime.getMinute(), refinedTime.getSecond(), ",", refinedTime.getNano());
                            break;
                        case DURATION:
                            DurationValue refinedDuration = (DurationValue) value;
                            expectedConstant = new Neo4JDurationConstant(refinedDuration.toString());
                            break;
                        case POINT:
                            PointValue refinedPoint = (PointValue) value;

                            if (refinedPoint.coordinate().length == 2) {
                                expectedConstant = new Neo4JPointConstant(refinedPoint.coordinate()[0], refinedPoint.coordinate()[1]);
                            } else {
                                expectedConstant = new Neo4JPointConstant(refinedPoint.coordinate()[0], refinedPoint.coordinate()[1], refinedPoint.coordinate()[2]);
                            }
                            break;
                        default:
                            throw new AssertionError(type);
                    }

                    CypherExpression branch = new CypherBinaryComparisonOperation(new CypherVariablePropertyAccess(String.format("n%d.%s", current, key)), expectedConstant, CypherBinaryComparisonOperation.BinaryComparisonOperator.EQUALS);
                    refinedWhere = new CypherBinaryLogicalOperation(refinedWhere, branch, CypherBinaryLogicalOperation.BinaryLogicalOperator.AND);
                }
            }
        }
    }

    private boolean verify(List<String> labels, List<Long> expectedIds, CypherExpression whereCondition) {
        ExpectedErrors errors = new ExpectedErrors();

        Neo4JUtil.addRegexErrors(errors);
        Neo4JUtil.addArithmeticErrors(errors);
        Neo4JUtil.addFunctionErrors(errors);

        StringBuilder refinedQuery = new StringBuilder("MATCH ");
        String separator = "";

        for (int i = 0; i < labels.size(); i++) {
            String label = labels.get(i);

            refinedQuery.append(separator);
            refinedQuery.append(String.format("(n%d:%s)", i, label));
            separator = ", ";
        }

        refinedQuery.append(" WHERE ");
        refinedQuery.append(CypherVisitor.asString(whereCondition));
        refinedQuery.append(" RETURN ");

        separator = "";
        for (int i = 0; i < labels.size(); i++) {
            refinedQuery.append(separator);
            refinedQuery.append(String.format("id(n%d)", i));
            separator = ", ";
        }

        List<Map<String, Object>> currentResults = new Neo4JQuery(refinedQuery.toString(), errors).executeAndGet(state);

        for (Map<String, Object> result : currentResults) {
            for (int i = 0; i < labels.size(); i++) {
                Long id = (Long) result.get(String.format("id(n%d)", i));
                Long expectedId = expectedIds.get(i);

                if (!Objects.equals(id, expectedId)) {
                    break;
                }
            }

            return true;
        }

        return false;
    }

}
