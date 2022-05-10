package kolmachikhin.firemessenger.presentation.viewmodel.search

import kolmachikhin.firemessenger.data.UserData

sealed class SearchState {
    class Loading : SearchState()
    class Loaded(
        val searchText: String,
        val updateSearchText: (String) -> Unit,
        val users: List<UserData>
    ) : SearchState()
}