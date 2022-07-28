package ch.ethz.ast.gdblancer.janus.schema;

import ch.ethz.ast.gdblancer.janus.JanusBugs;
import ch.ethz.ast.gdblancer.janus.gen.JanusValueGenerator;
import ch.ethz.ast.gdblancer.janus.schema.SimplePredicate.Type;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

import static ch.ethz.ast.gdblancer.janus.schema.JanusType.*;
import static ch.ethz.ast.gdblancer.janus.schema.SimplePredicate.Type.*;

public class PredicateGenerator {

    private static final int MAX_DEPTH = 3;

    private int currentDepth = 0;

    private PredicateGenerator() {}

    public static Predicate generateFor(JanusType type) {
        return new PredicateGenerator().generateForInternal(type);
    }

    private Predicate generateForInternal(JanusType type) {
        currentDepth++;
        PredicateType predicateType;

        if (currentDepth >= MAX_DEPTH) {
            predicateType = PredicateType.getRandomTerminatingType();
        } else {
            predicateType = PredicateType.getRandom();
        }

        switch (predicateType) {
            case RANGE_PREDICATE:
                return generateRangePredicate(type);
            case BINARY_PREDICATE:
                return generateBinaryPredicate(type);
            case UNARY_PREDICATE:
                return generateUnaryPredicate(type);
            case SIMPLE_PREDICATE:
                return generateSimplePredicate(type);
            default:
                throw new AssertionError(predicateType);
        }
    }

    private Predicate generateUnaryPredicate(JanusType type) {
        return new UnaryPredicate(UnaryPredicate.Type.getRandom(), generateForInternal(type));
    }

    private Predicate generateBinaryPredicate(JanusType type) {
        return new BinaryPredicate(BinaryPredicate.Type.getRandom(), generateForInternal(type), generateForInternal(type));
    }

    private Predicate generateSimplePredicate(JanusType type) {
        if (Set.of(STRING, CHARACTER, BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, DATE).contains(type)) {
            Type predicateType;

            if (type == STRING) {
                predicateType = getRandom();
            } else {
                predicateType = Randomization.fromOptions(
                        EQUALS, NOT_EQUALS, GREATER_THAN,
                        LESS_THAN, GREATER_THAN_EQUAL, LESS_THAN_EQUAL);
            }

            Object value;

            if (JanusBugs.bug3154) {
                value = JanusValueGenerator.generateWithoutMaxMinValues(type);
            } else {
                value = JanusValueGenerator.generate(type);
            }

            return new SimplePredicate(predicateType, value);
        } else if (type.equals(UUID) || type.equals(BOOLEAN)) {
            return new SimplePredicate(
                    Randomization.fromOptions(EQUALS, NOT_EQUALS),
                    JanusValueGenerator.generate(type)
            );
        } else {
            throw new AssertionError(type);
        }
    }

    // TODO: Maybe only generate "valid" ranges
    private Predicate generateRangePredicate(JanusType type) {
        if (JanusBugs.bug3154) {
            return new RangePredicate(
                    RangePredicate.Type.getRandom(),
                    JanusValueGenerator.generateWithoutMaxMinValues(type),
                    JanusValueGenerator.generateWithoutMaxMinValues(type));
        } else {
            return new RangePredicate(
                    RangePredicate.Type.getRandom(),
                    JanusValueGenerator.generate(type),
                    JanusValueGenerator.generate(type));
        }
    }

    private enum PredicateType {
        RANGE_PREDICATE,
        SIMPLE_PREDICATE,
        BINARY_PREDICATE,
        UNARY_PREDICATE;

        static PredicateType getRandom() {
            return Randomization.fromOptions(values());
        }

        static PredicateType getRandomTerminatingType() {
            return Randomization.fromOptions(RANGE_PREDICATE, SIMPLE_PREDICATE);
        }
    }

}
