package kolmachikhin.firemessenger.auth

sealed class EmailRegistrationResult {
    class Success : EmailRegistrationResult()
    sealed class Failed : EmailRegistrationResult() {
        class EmailAlreadyUsed(val email: String) : Failed()
        class ConnectionError : Failed()
        class Unknown : Failed()
    }
}