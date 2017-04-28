package edu.rochester.avarela.komics.graphics.prebuilt

import edu.rochester.avarela.komics.graphics.Actor
import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D

class TextStage(scene: Scene,
                position: Pair<Double, Double>,
                dimensions: Dimension,
                lines: List<String>) : Stage(scene, position, dimensions) {

    override val actors: List<Actor> = listOf(object: Actor(0.0 to 0.0, this, dimensions) {

        open val spacing = dimensions.height / lines.size

        override fun paint(g: Graphics2D) {
            g.color = Color.BLACK
            var currY = g.fontMetrics.height
            for (line in lines) {
                g.drawString(line, 0, currY)
                currY += spacing
            }
        }
    })
}