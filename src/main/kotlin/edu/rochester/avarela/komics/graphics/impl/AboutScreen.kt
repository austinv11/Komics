package edu.rochester.avarela.komics.graphics.impl

import edu.rochester.avarela.komics.graphics.Actor
import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import edu.rochester.avarela.komics.graphics.prebuilt.TextStage
import edu.rochester.avarela.komics.localization
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D

class AboutScene(val parent: MenuScene) : Scene(parent.window, parent.dimensions) {

    override val background: Color = parent.background

    override val stages: List<Stage> = listOf(object: Stage(this, 0.0 to 0.0, dimensions) {
            override val actors: List<Actor> = listOf(object: Actor(0.0 to 0.0, this, dimensions) { //Dummy, just so I could allow for clicks to exit the scene
                    override fun paint(g: Graphics2D) {}

                    override fun onClick(mouseNumber: Int, coords: Pair<Int, Int>) {
                        window.scene = parent
                    }
                })
            },
        TextStage(this, 30.0 to 30.0, Dimension(dimensions.width, (dimensions.height*.6).toInt()), localization["gui.about.text"].split("\\n")))

    override fun onKeyInput(char: Char) {
        window.scene = parent
    }
}