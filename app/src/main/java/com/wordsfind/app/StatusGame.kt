package com.wordsfind.app

sealed interface StatusGame {
    object Pause: StatusGame
    object Quiz: StatusGame
}