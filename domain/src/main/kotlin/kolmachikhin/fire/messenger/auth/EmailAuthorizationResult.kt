package kolmachikhin.fire.messenger.auth

sealed class EmailAuthorizationResult {
    class Success : EmailAuthorizationResult()
    sealed class Failed : EmailAuthorizationResult() {
        class ConnectionError : Failed()
        class Unknown : Failed()
    }
}