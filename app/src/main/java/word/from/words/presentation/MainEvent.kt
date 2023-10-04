package word.from.words.presentation

sealed class MainEvent {
    object OnReset:MainEvent()
    object OnPlay:MainEvent()
    class OnSelect(val variant: String):MainEvent()
}
