package kolmachikhin.firemessenger.auth

import kolmachikhin.firemessenger.validation.Correct

interface EmailRegistrar {

    suspend fun register(
        nickname: Correct<String>,
        email: Correct<String>,
        password: Correct<String>
    ): EmailRegistrationResult

}