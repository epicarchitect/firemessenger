package kolmachikhin.firemessenger.auth

import kolmachikhin.firemessenger.validation.Correct

interface EmailAuthorizer {

    suspend fun authorize(
        email: Correct<String>,
        password: Correct<String>
    ): EmailAuthorizationResult

}