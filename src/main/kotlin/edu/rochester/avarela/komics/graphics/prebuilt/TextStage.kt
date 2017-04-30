package edu.rochester.avarela.komics.graphics.prebuilt

import edu.rochester.avarela.komics.centerText
import edu.rochester.avarela.komics.graphics.Actor
import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D

class TextStage(scene: Scene,
                position: Pair<Double, Double>,
                dimensions: Dimension,
                @Volatile var lines: List<String>,
                val shouldCenter: Boolean = false) : Stage(scene, position, dimensions) {

    override val actors: List<Actor> = listOf(object: Actor(0.0 to 0.0, this, dimensions) {

        open val spacing = dimensions.height / lines.size

        override fun paint(g: Graphics2D) {
            g.color = Color.BLACK
            var currY = g.fontMetrics.height
            for (line in lines) {
                if (shouldCenter)
                    g.centerText(line, 0F, currY.toFloat())
                else
                    g.drawString(line, 0, currY)
                currY += spacing
            }
        }
    })
}

class TextActor(position: Pair<Double, Double>,
                stage: Stage,
                dimensions: Dimension,
                @Volatile var lines: List<String>,
                val shouldCenter: Boolean = false) : Actor(position, stage, dimensions) {

    open val spacing = dimensions.height / lines.size

    override fun paint(g: Graphics2D) {
        g.color = Color.BLACK
        var currY = g.fontMetrics.height
        for (line in lines) {
            if (shouldCenter)
                g.centerText(line, 0F, currY.toFloat())
            else
                g.drawString(line, 0, currY)
            currY += spacing
        }
    }
}