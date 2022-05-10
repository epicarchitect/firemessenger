package kolmachikhin.firemessenger.firebaseapp.di

import kolmachikhin.firemessenger.firebaseapp.repository.FirebaseCurrentUserRepository
import kolmachikhin.firemessenger.firebaseapp.repository.FirebaseUsersRepository
import kolmachikhin.firemessenger.repository.CurrentUserRepository
import kolmachikhin.firemessenger.repository.UsersRepository
import org.koin.dsl.module

fun singletonsModule() =  module(createdAtStart = true) {
    single<CurrentUserRepository> { FirebaseCurrentUserRepository() }
    single<UsersRepository> { FirebaseUsersRepository() }
}