package edu.rochester.avarela.komics.graphics

import java.awt.Graphics2D

abstract class Actor(val position: Pair<Double, Double>, val stage: Stage) {

    val window = stage.window

    fun draw(g: Graphics2D) {
        val (dX, dY) = position
        g.translate(dX, dY)
        paint(g)
    }

    abstract fun paint(g: Graphics2D)

    open fun onUpdate(dT: Long) {

    }
}