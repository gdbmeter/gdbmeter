package ch.ethz.ast.gdbmeter.common;

public interface Oracle {

    void check();

    /**
     * Hook that is called before the graph is generated.
     * Can be used to enable oracle specific bugs that need to be considered during generation.
     */
    default void onGenerate() {}

    /**
     * Hook that is called before the oracle is run the first time.
     * Can be used to enable oracle specific bugs that need to be considered during execution.
     */
    default void onStart() {}

    /**
     * Hook that is called when the oracle terminates.
     * Can be used to disable oracle specific bugs.
     */
    default void onComplete() {}

}
