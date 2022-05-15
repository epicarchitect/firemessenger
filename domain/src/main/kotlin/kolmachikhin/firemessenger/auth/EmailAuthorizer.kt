package kolmachikhin.firemessenger.auth

import kolmachikhin.alexander.validation.Correct

interface EmailAuthorizer {

    suspend fun authorize(
        email: Correct<String>,
        password: Correct<String>
    ): EmailAuthorizationResult

}