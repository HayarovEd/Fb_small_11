package word.from.words

data class MainState(
    val currentWord: String = words.first(),
    val allWords: List<String> = words,
    val score: Int = 0,
    val timer: Int = 3,
    val message: String = "",
    val statusGame: StatusGame = StatusGame.Pause
)