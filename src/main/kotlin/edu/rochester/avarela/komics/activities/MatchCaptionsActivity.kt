package edu.rochester.avarela.komics.activities

import edu.rochester.avarela.komics.SpecialChars
import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import edu.rochester.avarela.komics.graphics.Window
import edu.rochester.avarela.komics.graphics.prebuilt.ComicStage
import edu.rochester.avarela.komics.graphics.prebuilt.TextStage
import edu.rochester.avarela.komics.graphics.prebuilt.ToggleableButtonActor
import edu.rochester.avarela.komics.localization
import edu.rochester.avarela.komics.target
import java.awt.Color
import java.awt.Dialog
import java.awt.Dimension
import javax.swing.JDialog
import javax.swing.JOptionPane

class MatchCaptionsActivity(window: Window) : Scene(window, window.dimensions) {
    val BUTTON_HEIGHT = 40
    val BUTTON_WIDTH = 238
    val BUTTON_SPACING = 3

    var answerTracker = 0
    var correctCount = 0

    override val background: Color = Color.WHITE
    override val stages: List<Stage> = listOf(ComicStage(this, dimensions.width * .1 to dimensions.height * .1, Dimension((dimensions.width * .8F).toInt(), (dimensions.height * .5F).toInt()), "museum.png"),
            TextStage(this, dimensions.width / 2.0 to 10.0, dimensions, listOf(localization["mc"]), true),
            MultipleChoiceStage(this, dimensions.width * .1 to dimensions.height * .7, listOf(target["mc.panel1.1"] to true, target["mc.panel1.2"] to false, target["mc.panel1.3"] to false)),
            MultipleChoiceStage(this, dimensions.width * .1 + (BUTTON_WIDTH) + BUTTON_SPACING to dimensions.height * .7, listOf(target["mc.panel2.1"] to false, target["mc.panel2.2"] to true, target["mc.panel2.3"] to false)),
            MultipleChoiceStage(this, dimensions.width * .1 + (BUTTON_WIDTH * 2) + (BUTTON_SPACING*2) to dimensions.height * .7, listOf(target["mc.panel3.1"] to true, target["mc.panel3.2"] to false, target["mc.panel3.3"] to false)))

    override fun onKeyPressed(char: Char) {
        if (char == SpecialChars.ESC || char == SpecialChars.ENTER)
            window.scene = window.previousScene
    }

    inner class MultipleChoiceStage(scene: Scene,
                                    position: Pair<Double, Double>,
                                    val choices: List<Pair<String, Boolean>>) : Stage(scene, position, Dimension(BUTTON_WIDTH.toInt(), BUTTON_HEIGHT*3)) {

        var hasSelection: Boolean = false

        override val actors: List<Choice> = listOf(Choice(0.0 to 0.0, 0),
                Choice(0.0 to BUTTON_HEIGHT.toDouble(), 1),
                Choice(0.0 to BUTTON_HEIGHT * 2.0, 2))

        fun notifyButtons(choice: Int) {
            actors.forEachIndexed { index, choiceObj ->
                if (choices[index].second) {
                    choiceObj.defaultColor = Color.GREEN.brighter()
                } else {
                    choiceObj.defaultColor = Color.RED
                }

                if (choice == index) {
                    if (choiceObj.defaultColor != Color.RED)
                        correctCount++

                    choiceObj.defaultColor = choiceObj.defaultColor.darker().darker().darker().darker()
                }
            }

            if (++answerTracker == 3) {
                JOptionPane.showMessageDialog(window.frame, localization["gui.results.message"].format(correctCount, answerTracker), localization["gui.results"], JOptionPane.INFORMATION_MESSAGE)
                onKeyPressed(SpecialChars.ESC)
            }
        }

        inner class Choice(position: Pair<Double, Double>, val choice: Int): ToggleableButtonActor(position,
                this,
                Dimension(BUTTON_WIDTH.toInt(), BUTTON_HEIGHT),
                choices[choice].first,
                Color.LIGHT_GRAY, {}) {

            override fun onButtonPress() {
                if (!hasSelection) {
                    hasSelection = true
                    notifyButtons(choice)
                }
            }
        }

    }
}