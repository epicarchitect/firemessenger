package kolmachikhin.firemessenger.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kolmachikhin.firemessenger.data.UserData
import kolmachikhin.firemessenger.presentation.R
import kolmachikhin.firemessenger.presentation.viewmodel.search.SearchState

@Composable
fun Search(
    state: SearchState,
    onSelected: (UserData) -> Unit
) {
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

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(state.users) { user ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable {
                                            onSelected(user)
                                        }
                                ) {
                                    Text(
                                        modifier = Modifier.padding(16.dp),
                                        text = user.nickname,
                                        style = MaterialTheme.typography.body1
                                    )
                                }
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