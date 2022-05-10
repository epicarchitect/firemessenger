package kolmachikhin.firemessenger.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kolmachikhin.firemessenger.presentation.R
import kolmachikhin.firemessenger.presentation.viewmodel.search.SearchState

@Composable
fun Search(state: SearchState) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            is SearchState.Loaded -> {
                Column {
                    TextField(
                        value = state.searchText,
                        onValueChange = {
                            state.updateSearchText(it)
                        }
                    )

                    LazyColumn {
                        items(state.users) { user ->
                            Card {
                                Text(user.nickname)
                            }
                        }
                    }
                }
            }
            is SearchState.Loading -> {
                Loading(stringResource(R.string.loading))
            }
        }
    }
}