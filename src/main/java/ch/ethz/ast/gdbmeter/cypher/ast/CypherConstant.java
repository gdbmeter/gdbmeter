package ch.ethz.ast.gdbmeter.cypher.ast;

import ch.ethz.ast.gdbmeter.cypher.CypherUtil;

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

        public IntegerOctalConstant(long value) {
            super(value);
        }

        @Override
        public String getTextRepresentation() {
            StringBuilder sb = new StringBuilder();
            long modifiedValue = value;

            if (value < 0) {
                sb.append("-");
                modifiedValue *= -1;
            }

            return sb.append("0o")
                    .append(Long.toOctalString(modifiedValue))
                    .toString();
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

}
