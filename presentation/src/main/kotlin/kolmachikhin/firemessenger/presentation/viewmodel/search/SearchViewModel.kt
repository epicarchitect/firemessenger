package kolmachikhin.firemessenger.presentation.viewmodel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.firemessenger.repository.MyUserRepository
import kolmachikhin.firemessenger.repository.MyUserState
import kolmachikhin.firemessenger.repository.UsersRepository
import kolmachikhin.firemessenger.repository.UsersState
import kotlinx.coroutines.flow.*

class SearchViewModel(
    myUserRepository: MyUserRepository,
    usersRepository: UsersRepository
) : ViewModel() {

    private val searchTextState = MutableStateFlow("")

    val state = combine(
        usersRepository.state,
        searchTextState,
        myUserRepository.state.filterIsInstance<MyUserState.Loaded>().map { it.user }
    ) { usersState, searchText, currentUser ->
        when (usersState) {
            is UsersState.Loaded -> SearchState.Loaded(
                searchText = searchText,
                updateSearchText = {
                    searchTextState.value = it
                },
                users = usersState.users.let {
                    if (searchText.isEmpty()) it
                    else it.filter { it.nickname.contains(searchText, true) }
                }.filter { it.id != currentUser.id }
            )
            is UsersState.Loading -> SearchState.Loading()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        SearchState.Loading()
    )

}