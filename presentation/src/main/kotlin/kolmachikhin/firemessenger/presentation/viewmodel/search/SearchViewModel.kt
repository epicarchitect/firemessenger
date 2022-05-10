package kolmachikhin.firemessenger.presentation.viewmodel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolmachikhin.firemessenger.repository.UsersRepository
import kolmachikhin.firemessenger.repository.UsersState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(
    usersRepository: UsersRepository
) : ViewModel() {

    private val searchTextState = MutableStateFlow("")

    val state = combine(usersRepository.state, searchTextState) { usersState, searchText ->
        when (usersState) {
            is UsersState.Loaded -> SearchState.Loaded(
                searchText = searchText,
                updateSearchText = {
                    searchTextState.value = it
                },
                users = usersState.users.let {
                    if (searchText.isEmpty()) it
                    else it.filter { it.nickname.contains(searchText, ignoreCase = true) }
                }
            )
            is UsersState.Loading -> SearchState.Loading()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        SearchState.Loading()
    )

}