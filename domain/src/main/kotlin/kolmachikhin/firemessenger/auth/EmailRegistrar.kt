package kolmachikhin.firemessenger.auth

import kolmachikhin.alexander.validation.Correct

interface EmailRegistrar {

    suspend fun register(
        nickname: Correct<String>,
        email: Correct<String>,
        password: Correct<String>
    ): EmailRegistrationResult

}