package edu.rochester.avarela.komics.graphics

import java.awt.Dimension
import java.awt.Graphics2D

abstract class Actor(val position: Pair<Double, Double>, val stage: Stage, val dimensions: Dimension) {

    val window = stage.window
    open val keyInputRequiresSelection: Boolean = false
    public var isHovered: Boolean = false
    public var isSelected: Boolean = false

    fun draw(g: Graphics2D) {
        val (dX, dY) = position
        g.translate(dX, dY)
        paint(g)
    }

    abstract fun paint(g: Graphics2D)

    open fun onUpdate(dT: Long) {

    }

    open fun onHover(coords: Pair<Int, Int>) {
        isHovered = true
    }

    open fun onHoverExit() {
        isHovered = false
    }

    open fun onClick(mouseNumber: Int, coords: Pair<Int, Int>) {

    }

    open fun onMousePressed(mouseNumber: Int, coords: Pair<Int, Int>) {
        isSelected = true
    }

    open fun onMouseReleased(mouseNumber: Int, coords: Pair<Int, Int>) {
        isSelected = false
    }

    open fun onKeyInput(char: Char) {

    }

    open fun onKeyPressed(char: Char) {

    }

    open fun onKeyReleased(char: Char) {

    }
}