package edu.rochester.avarela.komics

import com.almasb.fxgl.app.ApplicationMode
import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.settings.GameSettings
import edu.rochester.avarela.komics.lang.Languages
import javafx.application.Application

fun main(args: Array<String>) {
    System.setProperty("log4j.skipJansi", "true")
    Application.launch(Game::class.java, *args)
}

class Game : GameApplication() {

    init {
        INSTANCE = this
    }

    override fun initSettings(settings: GameSettings) {
        settings.width = 900
        settings.height = 600
        settings.title = "Komics"
        settings.version = "1.0.0"
        settings.isProfilingEnabled = false  // turn off fps
        settings.isCloseConfirmation = false // turn off exit dialog
        settings.isIntroEnabled = false      // turn off intro
//        settings.isMenuEnabled = false       // turn off menus
        settings.applicationMode = ApplicationMode.DEVELOPER
    }

    override fun initInput() {

    }

    override fun initAssets() {
        println(Languages["en_US"]["gui.test"])
    }

    override fun initGame() {

    }

    override fun initPhysics() {

    }

    override fun initUI() {

    }

    override fun onUpdate(tpf: Double) {

    }

    companion object {
        var INSTANCE: GameApplication? = null
    }
}


val GAME: GameApplication by lazy { Game.INSTANCE!! }