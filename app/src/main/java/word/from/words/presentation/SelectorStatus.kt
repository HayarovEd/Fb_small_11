package word.from.words.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SelectorStatus (
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    when (val result = state.value.status) {
        ApplicationStatus.Loading -> {
            LoadingScreen()
        }
        ApplicationStatus.Mock -> {
            Content(
                score = state.value.score,
                timer = state.value.timer,
                currentWord = state.value.currentWord,
                allWords = state.value.allWords,
                message = state.value.message,
                statusGame = state.value.statusGame,
                onEvent = viewModel::onEvent
            )
        }
        is ApplicationStatus.Succsess -> {
            WebScreen(
                url = result.url
            )
        }

        is ApplicationStatus.Error -> {
            ErrorScreen(error = result.error)
        }
    }
}