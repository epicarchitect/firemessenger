package kolmachikhin.fire.messenger.auth

import kolmachikhin.fire.messenger.validation.Correct

interface EmailAuthorizer {

    suspend fun authorize(
        email: Correct<String>,
        password: Correct<String>
    ): EmailAuthorizationResult

}