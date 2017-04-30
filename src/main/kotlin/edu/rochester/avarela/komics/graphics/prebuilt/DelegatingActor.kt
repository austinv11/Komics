package edu.rochester.avarela.komics.graphics.prebuilt

import edu.rochester.avarela.komics.graphics.Actor
import edu.rochester.avarela.komics.graphics.Stage
import java.awt.Dimension
import java.awt.Graphics2D

open class DelegatingActor(position: Pair<Double, Double>, stage: Stage, dimensions: Dimension, @Volatile var actor: Actor) : Actor(position, stage, dimensions) {
    
    override fun paint(g: Graphics2D) {
        actor.paint(g)
    }

    override fun onUpdate(dT: Long) {
        actor.onUpdate(dT)
    }

    override fun onHover(coords: Pair<Int, Int>) {
        actor.onHover(coords)
    }

    override fun onHoverExit() {
        actor.onHoverExit()
    }

    override fun onClick(mouseNumber: Int, coords: Pair<Int, Int>) {
        actor.onClick(mouseNumber, coords)
    }

    override fun onMousePressed(mouseNumber: Int, coords: Pair<Int, Int>) {
        actor.onMousePressed(mouseNumber, coords)
    }

    override fun onMouseReleased(mouseNumber: Int, coords: Pair<Int, Int>) {
        actor.onMouseReleased(mouseNumber, coords)
    }

    override fun onScroll(scrollAmount: Int, coords: Pair<Int, Int>) {
        actor.onScroll(scrollAmount, coords)
    }

    override fun onKeyInput(char: Char) {
        actor.onKeyInput(char)
    }

    override fun onKeyPressed(char: Char) {
        actor.onKeyPressed(char)
    }

    override fun onKeyReleased(char: Char) {
        actor.onKeyReleased(char)
    }
}