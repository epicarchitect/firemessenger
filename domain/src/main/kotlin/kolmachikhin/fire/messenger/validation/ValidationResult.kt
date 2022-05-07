package kolmachikhin.fire.messenger.validation

sealed class ValidationResult<out DATA, out INCORRECT_REASON> {
    abstract val data: DATA
}

data class Correct<out DATA>(
    override val data: DATA
) : ValidationResult<DATA, Nothing>()

data class Incorrect<out DATA, out REASON>(
    override val data: DATA,
    val reason: REASON
) : ValidationResult<DATA, REASON>()