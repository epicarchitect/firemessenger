package kolmachikhin.fire.messenger.auth

import kolmachikhin.fire.messenger.validation.Correct

abstract class EmailRegistrar {

    abstract suspend fun register(
        nickname: Correct<String>,
        email: Correct<String>,
        password: Correct<String>
    ): Result

    sealed class Result {
        class Success : Result()
        sealed class Failed : Result() {
            class EmailAlreadyUsed(val email: String) : Failed()
            class ConnectionError : Failed()
            class Unknown : Failed()
        }
    }
}