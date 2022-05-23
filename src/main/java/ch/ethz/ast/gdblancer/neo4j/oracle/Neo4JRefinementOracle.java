package ch.ethz.ast.gdblancer.neo4j.oracle;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.gen.ast.*;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
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
    private final Neo4JDBSchema schema;

    public Neo4JRefinementOracle(GlobalState<Neo4JConnection> state, Neo4JDBSchema schema) {
        this.state = state;
        this.schema = schema;
    }

    @Override
    public void check() {
        int amount = Randomization.nextInt(2, 5);
        List<String> labels = new ArrayList<>(10);
        Neo4JExpression whereExpression = new Neo4JConstant.BooleanConstant(true);

        ExpectedErrors errors = new ExpectedErrors();

        Neo4JDBUtil.addRegexErrors(errors);
        Neo4JDBUtil.addArithmeticErrors(errors);
        Neo4JDBUtil.addFunctionErrors(errors);

        for (int i = 0; i < amount; i++) {
            labels.add(schema.getRandomLabel());
        }

        // Create match query
        StringBuilder query = new StringBuilder("MATCH ");
        String separator = "";

        for (int i = 0; i < amount; i++) {
            String label = labels.get(i);
            Neo4JDBEntity entity = schema.getEntityByLabel(label);

            query.append(separator);
            query.append(String.format("(n%d:%s)", i, label));
            separator = ", ";
            Neo4JExpression expression = Neo4JExpressionGenerator.generateExpression(Map.of(String.format("n%d", i), entity), Neo4JType.BOOLEAN);
            whereExpression = new Neo4JBinaryLogicalOperation(whereExpression, expression, Neo4JBinaryLogicalOperation.BinaryLogicalOperator.AND);
        }

        query.append(" WHERE ");
        query.append(Neo4JVisitor.asString(whereExpression));
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

        Neo4JExpression refinedWhere = new Neo4JConstant.BooleanConstant(true);

        for (int current = 0; current < amount; current++) {
            if (!verify(labels, expectedIds, refinedWhere)) {
                throw new AssertionError("Not present");
            } else {
                // refine current
                Map<String, Object> properties = (Map<String, Object>) pivotResult.get(String.format("properties(n%d)", current));
                String label = labels.get(current);
                Neo4JDBEntity entity = schema.getEntityByLabel(label);
                String key = Randomization.fromSet(entity.getAvailableProperties().keySet());
                Neo4JType type = entity.getAvailableProperties().get(key);

                Object value = properties.get(key);
                Neo4JExpression expectedConstant;

                if (value == null) {
                    Neo4JExpression branch = new Neo4JPostfixOperation(new Neo4JVariablePropertyAccess(String.format("n%d.%s", current, key)), Neo4JPostfixOperation.PostfixOperator.IS_NULL);
                    refinedWhere = new Neo4JBinaryLogicalOperation(refinedWhere, branch, Neo4JBinaryLogicalOperation.BinaryLogicalOperator.AND);
                } else {
                    switch (type) {
                        case BOOLEAN:
                            expectedConstant = new Neo4JConstant.BooleanConstant((Boolean) value);
                            break;
                        case FLOAT:
                            expectedConstant = new Neo4JConstant.FloatConstant((Double) value);
                            break;
                        case INTEGER:
                            expectedConstant = new Neo4JConstant.IntegerConstant((Long) value);
                            break;
                        case STRING:
                            expectedConstant = new Neo4JConstant.StringConstant((String) value);
                            break;
                        case DATE:
                            LocalDate refinedDate = (LocalDate) value;
                            expectedConstant = new Neo4JConstant.DateConstant(false, refinedDate.getYear(), refinedDate.getMonthValue(), refinedDate.getDayOfMonth());
                            break;
                        case LOCAL_TIME:
                            OffsetTime refinedTime = (OffsetTime) value;
                            expectedConstant = new Neo4JConstant.LocalTimeConstant(refinedTime.getHour(), "", refinedTime.getMinute(), refinedTime.getSecond(), ",", refinedTime.getNano());
                            break;
                        case DURATION:
                            DurationValue refinedDuration = (DurationValue) value;
                            expectedConstant = new Neo4JConstant.DurationConstant(refinedDuration.toString());
                            break;
                        case POINT:
                            PointValue refinedPoint = (PointValue) value;

                            if (refinedPoint.coordinate().length == 2) {
                                expectedConstant = new Neo4JConstant.PointConstant(refinedPoint.coordinate()[0], refinedPoint.coordinate()[1]);
                            } else {
                                expectedConstant = new Neo4JConstant.PointConstant(refinedPoint.coordinate()[0], refinedPoint.coordinate()[1], refinedPoint.coordinate()[2]);
                            }
                            break;
                        default:
                            throw new AssertionError(type);
                    }

                    Neo4JExpression branch = new Neo4JBinaryComparisonOperation(new Neo4JVariablePropertyAccess(String.format("n%d.%s", current, key)), expectedConstant, Neo4JBinaryComparisonOperation.BinaryComparisonOperator.EQUALS);
                    refinedWhere = new Neo4JBinaryLogicalOperation(refinedWhere, branch, Neo4JBinaryLogicalOperation.BinaryLogicalOperator.AND);
                }
            }
        }
    }

    private boolean verify(List<String> labels, List<Long> expectedIds, Neo4JExpression whereCondition) {
        ExpectedErrors errors = new ExpectedErrors();

        Neo4JDBUtil.addRegexErrors(errors);
        Neo4JDBUtil.addArithmeticErrors(errors);
        Neo4JDBUtil.addFunctionErrors(errors);

        StringBuilder refinedQuery = new StringBuilder("MATCH ");
        String separator = "";

        for (int i = 0; i < labels.size(); i++) {
            String label = labels.get(i);

            refinedQuery.append(separator);
            refinedQuery.append(String.format("(n%d:%s)", i, label));
            separator = ", ";
        }

        refinedQuery.append(" WHERE ");
        refinedQuery.append(Neo4JVisitor.asString(whereCondition));
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
