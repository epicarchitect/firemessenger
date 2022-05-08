package kolmachikhin.fire.messenger.authorization

import kolmachikhin.fire.messenger.validation.Correct

interface EmailAuthorizer {


    suspend fun authorize(
        email: Correct<String>,
        password: Correct<String>
    ): Result

    sealed class Result {
        class Success : Result()
        sealed class Failed : Result() {
            class ConnectionError() : Failed()
            class Unknown : Failed()
        }
    }
}