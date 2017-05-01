package edu.rochester.avarela.komics.activities

import edu.rochester.avarela.komics.SpecialChars
import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import edu.rochester.avarela.komics.graphics.Window
import edu.rochester.avarela.komics.graphics.prebuilt.ComicStage
import edu.rochester.avarela.komics.graphics.prebuilt.PaintingStage
import edu.rochester.avarela.komics.graphics.prebuilt.TextActor
import edu.rochester.avarela.komics.graphics.prebuilt.TextStage
import edu.rochester.avarela.komics.localization
import edu.rochester.avarela.komics.scale
import edu.rochester.avarela.komics.target
import java.awt.Color

class FillThePanelActivity(window: Window) : Scene(window, window.dimensions) {
    override val background: Color = Color.WHITE
    override val stages: List<Stage> = listOf(ComicStage(this, dimensions.width * .1 to dimensions.height * .1, dimensions.scale(.8F), "fill.png"),
            TextStage(this, dimensions.width / 2.0 to 10.0, dimensions, listOf(localization["ftp"]), true),
            PaintingStage(this, 0.0 to 0.0, dimensions))
    init {
        with(stages[0] as ComicStage) {
            text += TextActor(65.0 to 35.0, this, this.dimensions, listOf(target["ftp.panel1"]))
        }
    }

    override fun onKeyPressed(char: Char) {
        if (char == SpecialChars.ESC || char == SpecialChars.ENTER)
            window.scene = window.previousScene
    }
}