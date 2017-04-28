package edu.rochester.avarela.komics.graphics.impl

import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import edu.rochester.avarela.komics.graphics.prebuilt.TextStage
import edu.rochester.avarela.komics.localization
import java.awt.Color
import java.awt.Dimension

class AboutScene(val parent: MenuScene) : Scene(parent.window, parent.dimensions) {

    override val background: Color = parent.background

    override val stages: List<Stage> = listOf(TextStage(this, 30.0 to 30.0, Dimension(dimensions.width, (dimensions.height*.6).toInt()), localization["gui.about.text"].split("\\n")))

    override fun onKeyInput(char: Char) {
        window.scene = parent
    }
}