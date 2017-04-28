package edu.rochester.avarela.komics.graphics

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D

abstract class Scene(val window: Window, val dimensions: Dimension = window.dimensions) {

    fun draw(g: Graphics2D) {
        g.color = background
        g.fillRect(0, 0, dimensions.width, dimensions.height)

        stages.forEach { it.draw(g.create() as Graphics2D) }
    }

    fun onUpdate(dT: Long) {
        stages.forEach { it.onUpdate(dT) }
    }

    open fun onKeyInput(char: Char) {

    }

    open fun onKeyPressed(char: Char) {

    }

    open fun onKeyReleased(char: Char) {

    }

    abstract val background: Color

    abstract val stages: List<Stage>
}