import org.junit.jupiter.api.parallel.ExecutionMode

runner {
    parallel {
        enabled true
        fixed(10)
        defaultExecutionMode = ExecutionMode.SAME_THREAD
    }
}