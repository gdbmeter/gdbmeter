package ch.ethz.ast.gdbmeter.neo4j.ast;

import ch.ethz.ast.gdbmeter.cypher.ast.CypherConstant;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;

import java.time.Year;

public class Neo4JDateConstant extends CypherConstant {

    private final boolean useSeparator;
    private final int year;
    private final int month;
    private final int day;

    public Neo4JDateConstant(boolean useSeparator, int year, int month, int day) {
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
