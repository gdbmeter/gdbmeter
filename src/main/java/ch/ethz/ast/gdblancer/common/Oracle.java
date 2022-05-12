package ch.ethz.ast.gdblancer.common;

public interface Oracle {

    void check();

    default void onStart() {}

    default void onComplete() {}

}
