package kolmachikhin.firemessenger.firebaseapp.di

import kolmachikhin.firemessenger.auth.EmailAuthorizer
import kolmachikhin.firemessenger.auth.EmailRegistrar
import kolmachikhin.firemessenger.firebaseapp.auth.FirebaseEmailAuthorizer
import kolmachikhin.firemessenger.firebaseapp.auth.FirebaseEmailRegistrar
import kolmachikhin.firemessenger.validation.EmailValidator
import kolmachikhin.firemessenger.validation.NicknameValidator
import kolmachikhin.firemessenger.validation.PasswordValidator
import org.koin.dsl.module

val factories = module {
    factory<EmailAuthorizer> { FirebaseEmailAuthorizer() }
    factory<EmailRegistrar> { FirebaseEmailRegistrar() }
    factory { NicknameValidator(maxNicknameLength = 25, minNicknameLength = 3) }
    factory { PasswordValidator(minPasswordLength = 6) }
    factory { EmailValidator() }
}