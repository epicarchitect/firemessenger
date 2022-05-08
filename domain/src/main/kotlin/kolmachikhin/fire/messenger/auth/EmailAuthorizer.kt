package kolmachikhin.fire.messenger.auth

import kolmachikhin.fire.messenger.validation.Correct

abstract class EmailAuthorizer {

    abstract suspend fun authorize(
        email: Correct<String>,
        password: Correct<String>
    ): Result

    sealed class Result {
        class Success : Result()
        sealed class Failed : Result() {
            class ConnectionError : Failed()
            class Unknown : Failed()
        }
    }
}