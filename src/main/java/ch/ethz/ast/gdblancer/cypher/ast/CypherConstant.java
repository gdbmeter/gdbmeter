package ch.ethz.ast.gdblancer.cypher.ast;

import ch.ethz.ast.gdblancer.cypher.CypherUtil;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.time.Year;
import java.util.Map;

public abstract class CypherConstant implements CypherExpression {

    public abstract String getTextRepresentation();

    public static class BooleanConstant extends CypherConstant {

        private final boolean value;

        public BooleanConstant(boolean value) {
            this.value = value;
        }

        @Override
        public String getTextRepresentation() {
            return value ? "true" : "false";
        }
    }

    public static class NullConstant extends CypherConstant {
        @Override
        public String getTextRepresentation() {
            return "null";
        }
    }

    public static class IntegerConstant extends CypherConstant {

        protected final long value;

        public IntegerConstant(long value) {
            this.value = value;
        }

        @Override
        public String getTextRepresentation() {
            return String.valueOf(value);
        }
    }

    public static class IntegerHexConstant extends IntegerConstant {

        public IntegerHexConstant(long value) {
            super(value);
        }

        @Override
        public String getTextRepresentation() {
            if (value < 0) {
                return "-0x" + Long.toHexString(-1 * value);
            } else {
                return "0x" + Long.toHexString(value);
            }
        }
    }

    public static class IntegerOctalConstant extends IntegerConstant {

        public enum OctalPrefix {
            ZERO,
            ZERO_O;

            public static OctalPrefix getRandom() {
                return Randomization.fromOptions(OctalPrefix.values());
            }
        }

        private final OctalPrefix prefix;

        public IntegerOctalConstant(long value, OctalPrefix prefix) {
            super(value);
            this.prefix = prefix;
        }

        @Override
        public String getTextRepresentation() {
            StringBuilder sb = new StringBuilder();
            long modifiedValue = value;

            if (value < 0) {
                sb.append("-");
                modifiedValue *= -1;
            }

            switch (prefix) {
                case ZERO:
                    sb.append("0").append(Long.toOctalString(modifiedValue));
                    break;
                case ZERO_O:
                    sb.append("0o").append(Long.toOctalString(modifiedValue));
                    break;
                default:
                    throw new AssertionError(prefix);
            }

            return sb.toString();
        }
    }

    public static class StringConstant extends CypherConstant {
        private final String value;

        public StringConstant(String value) {
            this.value = value;
        }

        @Override
        public String getTextRepresentation() {
            return CypherUtil.escape(value);
        }
    }

    // TODO: Should float actually store a double?
    public static class FloatConstant extends CypherConstant {

        private final double value;

        public FloatConstant(double value) {
            this.value = value;
        }

        @Override
        public String getTextRepresentation() {
            return String.valueOf(value);
        }
    }

    public static class DurationConstant extends CypherConstant {

        private String value;
        private Map<String, Long> datePart;
        private Map<String, Long> timePart;

        public DurationConstant(Map<String, Long> datePart, Map<String, Long> timePart) {
            this.datePart = datePart;
            this.timePart = timePart;

            if (datePart.isEmpty() && timePart.isEmpty()) {
                throw new IllegalArgumentException("Cannot create empty duration");
            }
        }

        public DurationConstant(String value) {
            this.value = value;
        }

        // We support the format: P[nY][nM][nW][nD][T[nH][nM][nS]]
        @Override
        public String getTextRepresentation() {
            if (value != null) {
                return String.format("duration('%s')", value);
            }

            StringBuilder representation = new StringBuilder();
            representation.append("duration('P");

            for (String current : datePart.keySet()) {
                representation.append(String.format("%d%s", datePart.get(current), current));
            }

            // Only 'T' alone is not considered valid
            if (!timePart.isEmpty()) {
                representation.append("T");

                for (String current : timePart.keySet()) {
                    representation.append(String.format("%d%s", timePart.get(current), current));
                }
            }

            representation.append("')");
            return representation.toString();
        }
    }

    public static class DateConstant extends CypherConstant {

        private final boolean useSeparator;
        private final int year;
        private final int month;
        private final int day;

        public DateConstant(boolean useSeparator, int year, int month, int day) {
            this.useSeparator = useSeparator;
            this.year = year;
            this.month = month;
            this.day = day;

            if (month == 2 && day >= 30) {
                throw new IgnoreMeException();
            }

            switch (month) {
                case 4:
                case 6:
                case 9:
                case 11:
                    if (day == 31) {
                        throw new IgnoreMeException();
                    }
                    break;
            }

            if (!Year.of(year).isLeap() && month == 2 && day >= 29) {
                throw new IgnoreMeException();
            }
        }

        @Override
        public String getTextRepresentation() {
            if (useSeparator) {
                return String.format("date('%04d-%02d-%02d')", year, month, day);
            } else {
                return String.format("date('%04d%02d%02d')", year, month, day);
            }
        }
    }

    public static class LocalTimeConstant extends CypherConstant {

        private final int hours;
        private final String separator;
        private final Integer minutes;
        private final Integer seconds;
        private final String nanoSecondSeparator;
        private final Integer nanoSeconds;

        public LocalTimeConstant(int hours,
                                 String separator,
                                 Integer minutes,
                                 Integer seconds,
                                 String nanoSecondSeparator,
                                 Integer nanoSeconds) {
            this.hours = hours;
            this.separator = separator;
            this.minutes = minutes;
            this.seconds = seconds;
            this.nanoSecondSeparator = nanoSecondSeparator;
            this.nanoSeconds = nanoSeconds;
        }

        @Override
        public String getTextRepresentation() {
            StringBuilder representation = new StringBuilder();
            representation.append("time('");
            representation.append(String.format("%02d", hours));

            if (minutes != null) {
                representation.append(separator);
                representation.append(String.format("%02d", minutes));

                if (seconds != null) {
                    representation.append(separator);
                    representation.append(String.format("%02d", seconds));

                    if (nanoSeconds != null) {
                        representation.append(nanoSecondSeparator);
                        representation.append(nanoSeconds);
                    }
                }
            }

            representation.append("')");

            return representation.toString();
        }
    }

    public static class PointConstant extends CypherConstant {

        private final double x;
        private final double y;
        private final Double z;

        public PointConstant(double x, double y) {
            this(x, y, null);
        }

        public PointConstant(double x, double y, Double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String getTextRepresentation() {
            if (z == null) {
                return String.format("point({ x: %f, y: %f })", this.x, this.y);
            } else {
                return String.format("point({ x: %f, y: %f, z: %f })", this.x, this.y, this.z);
            }
        }
    }


}
