package kolmachikhin.fire.messenger.authorization

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
            class EmailAlreadyUsed(val email: String) : Failed()
            class ConnectionError() : Failed()
            class Unknown : Failed()
        }
    }
}