package kolmachikhin.fire.messenger.registration

import kolmachikhin.fire.messenger.validation.Correct

interface EmailRegistrar {

    suspend fun register(
        firstName: Correct<String>,
        lastName: Correct<String>,
        email: Correct<String>,
        password: Correct<String>
    ): Result

    sealed class Result {
        class Success : Result()
        sealed class Failed : Result() {
            class UserAlreadyExists : Failed()
            class Unknown : Failed()
        }
    }
}