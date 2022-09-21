package ch.ethz.ast.gdbmeter.common;

public interface Generator<C extends Connection> {

    void generate(GlobalState<C> globalState);

}
