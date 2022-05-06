package kolmachikhin.fire.messenger.compose.koin

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.parametersOf

@Suppress("UNCHECKED_CAST")
@Composable
inline fun <reified VM : ViewModel> composeViewModel(vararg parameters: Any?) = viewModel<VM>(
    factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>) = get<VM>(parameters = parameters) as T
    }
)

inline fun <reified T : Any> get(vararg parameters: Any?) = GlobalContext.get().get<T> { parametersOf(parameters = parameters) }