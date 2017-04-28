package edu.rochester.avarela.komics.graphics.impl

import edu.rochester.avarela.komics.IS_DEBUG_MODE
import edu.rochester.avarela.komics.centerText
import edu.rochester.avarela.komics.graphics.Actor
import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import edu.rochester.avarela.komics.graphics.Window
import edu.rochester.avarela.komics.localization
import java.awt.Color
import java.awt.Graphics2D

class LoadScene(w: Window) : Scene(w) {

    override val background: Color = Color.BLACK
    override val stages: List<Stage> = listOf(LoadStage(this))
}

class LoadStage(scene: Scene) : Stage(scene, 0.toDouble() to 0.toDouble()) {
    override val actors: List<Actor> = listOf(ProgressBar())

    inner class ProgressBar(val width: Double = dimensions.width.toDouble() - 20.toDouble(), val height: Float = 20F)
        : Actor((dimensions.width.toDouble() - width) / 2.toDouble() to (dimensions.height.toDouble() - height.toDouble()) / 2.toDouble(),
            this, dimensions) {

        override fun paint(g: Graphics2D) {
            g.color = Color.RED
            val progress = Math.min(progress, 1F)
            g.fillRect(0, 0, (progress * width).toInt(), height.toInt())
            g.color = Color.WHITE
            g.drawRect(0, 0, width.toInt(), height.toInt())
            g.centerText("${localization["gui.loading"]} ${String.format("%.0f", progress * 100)}%", dimensions.width / 2F, -5F)
        }

        var progress: Float = 0F

        override fun onUpdate(dT: Long) {
            if (progress < 1.25F && !IS_DEBUG_MODE)
                progress += .01F
            else
                window.scene = MenuScene(window)
        }
    }
}