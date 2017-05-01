package edu.rochester.avarela.komics.activities

import edu.rochester.avarela.komics.*
import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import edu.rochester.avarela.komics.graphics.Window
import edu.rochester.avarela.komics.graphics.prebuilt.ComicStage
import edu.rochester.avarela.komics.graphics.prebuilt.TextActor
import edu.rochester.avarela.komics.graphics.prebuilt.TextStage
import java.awt.Color

class MakeATitleActivity(window: Window) : Scene(window, window.dimensions) {
    override val background: Color = Color.WHITE
    override val stages: List<Stage> = listOf(ComicStage(this, dimensions.width * .1 to dimensions.height * .1, dimensions.scale(.8F), "make_a_title.png"),
            TextStage(this, dimensions.width / 2.0 to 10.0, dimensions, listOf(localization["mat"]), true))
    init {
        with(stages[0] as ComicStage) {
            text += TextActor(110.0 to 70.0, this, this.dimensions, listOf(target["mat.panel1"]))
            text += TextActor(281.0 to 52.0, this, this.dimensions, listOf(target["mat.panel2.sound"]))
            text += TextActor(360.0 to 130.0, this, this.dimensions, listOf(target["mat.panel2"]))
            text += TextActor(595.0 to 48.0, this, this.dimensions, listOf(target["mat.panel3.bob"]))
            text += TextActor(550.0 to 275.0, this, this.dimensions, listOf(target["mat.panel3"]))
        }
    }

    override fun onKeyInput(char: Char) {
        if (char == SpecialChars.ENTER || char == SpecialChars.ESC) {
            window.scene = window.previousScene
        } else if (char == SpecialChars.BACKSPACE) {
            (stages[1] as TextStage).lines = listOf((stages[1] as TextStage).lines[0].removeLast)
        } else {
            if ((stages[1] as TextStage).lines[0] == localization["mat"]) {
                (stages[1] as TextStage).lines = listOf("")
            }
            (stages[1] as TextStage).lines = listOf((stages[1] as TextStage).lines[0] + char)
        }
    }
}