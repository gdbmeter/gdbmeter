package ch.ethz.ast.gdblancer.neo4j.gen.ast;

import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;
import org.apache.commons.text.StringEscapeUtils;

import java.time.Year;
import java.util.Map;

public abstract class Neo4JConstant implements Neo4JExpression {

    public abstract String getTextRepresentation();

    public static class BooleanConstant extends Neo4JConstant {

        private final boolean value;

        public BooleanConstant(boolean value) {
            this.value = value;
        }

        @Override
        public String getTextRepresentation() {
            return value ? "true" : "false";
        }
    }

    public static class NullConstant extends Neo4JConstant {
        @Override
        public String getTextRepresentation() {
            return "null";
        }
    }

    public static class IntegerConstant extends Neo4JConstant {

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

    public static class StringConstant extends Neo4JConstant {
        private final String value;

        public StringConstant(String value) {
            this.value = value;
        }

        @Override
        public String getTextRepresentation() {
            return "\"" + StringEscapeUtils.escapeJson(value) + "\"";
        }
    }

    public static class FloatConstant extends Neo4JConstant {

        private final float value;

        public FloatConstant(float value) {
            this.value = value;
        }

        @Override
        public String getTextRepresentation() {
            return String.valueOf(value);
        }
    }

    public static class DurationConstant extends Neo4JConstant {

        private final Map<String, Long> datePart;
        private final Map<String, Long> timePart;

        public DurationConstant(Map<String, Long> datePart, Map<String, Long> timePart) {
            this.datePart = datePart;
            this.timePart = timePart;

            if (datePart.isEmpty() && timePart.isEmpty()) {
                throw new IllegalArgumentException("Cannot create empty duration");
            }
        }

        // We support the format: P[nY][nM][nW][nD][T[nH][nM][nS]]
        @Override
        public String getTextRepresentation() {
            StringBuilder representation = new StringBuilder();
            representation.append("duration('P");

            for (String current : datePart.keySet()) {
                representation.append(String.format("%d%s", datePart.get(current), current));
            }

            // Only 'T' is not valid
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

    public static class DateConstant extends Neo4JConstant {

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

    public static class LocalTimeConstant extends Neo4JConstant {

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

    public static class PointConstant extends Neo4JConstant {

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
