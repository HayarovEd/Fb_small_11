package word.from.words

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(
        MainState()
    )
    val state = _state.asStateFlow()
    private var job: Job? = null

    fun reset() {
        job?.cancel()
        _state.value.copy(
            currentWord = words.first(),
            timer = 3,
            message = "",
            statusGame = StatusGame.Pause
        )
            .updateStateUI()
    }

    fun play() {
        _state.value.copy(
            statusGame = StatusGame.Quiz,
            currentWord = words.random(),
            allWords = words.shuffled()
        )
            .updateStateUI()
        restart()
    }

    fun click(variant: String) {
        if (variant==_state.value.currentWord) {
            val cS = _state.value.score
            _state.value.copy(
                score = cS+1,
                message = "Right"
            )
                .updateStateUI()
        } else {
            _state.value.copy(
                message = "Error"
            )
                .updateStateUI()
        }
        _state.value.copy(
            timer = 3,
            statusGame = StatusGame.Quiz,
            currentWord = words.random(),
            allWords = words.shuffled()
        )
            .updateStateUI()
        restart()
    }

    private fun restart () {
        val time = _state.value.timer
        job?.cancel()
        job = viewModelScope.launch {
            for (i in time downTo 0 step 1) {
                _state.value.copy(
                    timer = i
                )
                    .updateStateUI()
                delay(1000)
            }
            _state.value.copy(
                message = "Game completed",
                statusGame = StatusGame.Pause
            )
                .updateStateUI()
        }
        //play()
    }

    private fun MainState.updateStateUI() {
        _state.update {
            this
        }
    }
}