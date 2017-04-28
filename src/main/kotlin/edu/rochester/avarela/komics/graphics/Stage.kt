package edu.rochester.avarela.komics.graphics

import java.awt.Dimension
import java.awt.Graphics2D

abstract class Stage(val scene: Scene, val position: Pair<Double, Double>, val dimensions: Dimension = scene.dimensions) {

    val window = scene.window

    fun draw(g: Graphics2D) {
        val (dX, dY) = position
        g.translate(dX, dY)
//        g.clipRect(0, 0, dimensions.width, dimensions.height)

        actors.forEach { it.draw(g.create() as Graphics2D) }
    }

    fun onUpdate(dT: Long) {
        actors.forEach { it.onUpdate(dT) }
    }

    abstract val actors: List<Actor>
}