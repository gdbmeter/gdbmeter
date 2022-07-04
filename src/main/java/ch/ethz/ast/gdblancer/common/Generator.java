package ch.ethz.ast.gdblancer.common;

public interface Generator<C extends Connection> {

    void generate(GlobalState<C> globalState);

}
