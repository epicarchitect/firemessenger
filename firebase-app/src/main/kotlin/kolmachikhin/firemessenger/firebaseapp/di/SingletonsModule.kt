package kolmachikhin.firemessenger.firebaseapp.di

import kolmachikhin.firemessenger.firebaseapp.repository.MyChatsRepositoryImpl
import kolmachikhin.firemessenger.firebaseapp.repository.MyUserRepositoryImpl
import kolmachikhin.firemessenger.firebaseapp.repository.UsersRepositoryImpl
import kolmachikhin.firemessenger.repository.MyChatsRepository
import kolmachikhin.firemessenger.repository.MyUserRepository
import kolmachikhin.firemessenger.repository.UsersRepository
import org.koin.dsl.module

val singletons = module(createdAtStart = true) {
    single<MyUserRepository> { MyUserRepositoryImpl() }
    single<MyChatsRepository> { MyChatsRepositoryImpl(get()) }
    single<UsersRepository> { UsersRepositoryImpl() }
}