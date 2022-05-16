package ch.ethz.ast.gdblancer.common;

public interface Oracle {

    void check();

    default void onGenerate() {}

    default void onStart() {}

    default void onComplete() {}

}
