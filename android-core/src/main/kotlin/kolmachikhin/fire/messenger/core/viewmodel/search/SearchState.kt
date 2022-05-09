package kolmachikhin.fire.messenger.core.viewmodel.search

import kolmachikhin.fire.messenger.data.User

sealed class SearchState {
    class Loading : SearchState()
    class Loaded(
        val searchText: String,
        val updateSearchText: (String) -> Unit,
        val users: List<User>
    ) : SearchState()
}