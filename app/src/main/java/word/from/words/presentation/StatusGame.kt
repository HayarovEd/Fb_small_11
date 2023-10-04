package word.from.words.presentation

sealed interface StatusGame {
    object Pause: StatusGame
    object Quiz: StatusGame
}