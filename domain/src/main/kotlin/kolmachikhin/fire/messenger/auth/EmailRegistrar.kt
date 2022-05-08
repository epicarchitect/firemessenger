package kolmachikhin.fire.messenger.auth

import kolmachikhin.fire.messenger.validation.Correct

interface EmailRegistrar {

    suspend fun register(
        nickname: Correct<String>,
        email: Correct<String>,
        password: Correct<String>
    ): EmailRegistrationResult

}