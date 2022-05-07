package kolmachikhin.fire.messenger.registration

import kolmachikhin.fire.messenger.validation.Correct

interface EmailRegistrar {

    suspend fun register(
        email: Correct<String>,
        password: Correct<String>
    ): Result

    sealed class Result {
        class Success : Result()
        class Failed : Result()
    }
}