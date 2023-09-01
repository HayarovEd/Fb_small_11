package word.from.words

sealed interface StatusGame {
    object Pause: StatusGame
    object Quiz: StatusGame
}