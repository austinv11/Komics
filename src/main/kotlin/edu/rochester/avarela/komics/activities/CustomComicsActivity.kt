package edu.rochester.avarela.komics.activities

import edu.rochester.avarela.komics.SpecialChars
import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import edu.rochester.avarela.komics.graphics.Window
import edu.rochester.avarela.komics.graphics.prebuilt.PaintingStage
import java.awt.Color

class CustomComicsActivity(window: Window) : Scene(window, window.dimensions) {
    override val background: Color = Color.GRAY
    override val stages: List<Stage> = listOf(PaintingStage(this, 0.0 to 0.0, dimensions))

    override fun onKeyPressed(char: Char) {
        if (char == SpecialChars.ESC || char == SpecialChars.ENTER)
            window.scene = window.previousScene
    }
}