package edu.rochester.avarela.komics.graphics.prebuilt

import edu.rochester.avarela.komics.graphics.Actor
import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Dialog
import edu.rochester.avarela.komics.localization
import java.util.concurrent.CopyOnWriteArrayList
import java.util.stream.Collectors
import javax.swing.*

class PaintingStage(scene: Scene, position: Pair<Double, Double>, dimensions: Dimension) : Stage(scene, position, dimensions) {

    val BUTTON_DIMENSIONS = 32
    @Volatile var PIXEL_SIZE = 12
    @Volatile var currColor = Color.BLACK
    @Volatile var currAction = Actions.NONE

    val PaintSelector = object: ButtonActor(dimensions.getWidth() - BUTTON_DIMENSIONS - 1 to 0.0, this, Dimension(BUTTON_DIMENSIONS, BUTTON_DIMENSIONS), "", currColor) {

        override fun onButtonPress() {
            val chooser = JColorChooser(defaultColor)
            val dialog = JDialog(scene.window.frame, localization["gui.colors"], Dialog.ModalityType.APPLICATION_MODAL)
            dialog.add(chooser)
            dialog.pack()
            dialog.setLocationRelativeTo(null)
            dialog.isVisible = true
            currColor = chooser.color
        }

        val backing = ImageBoundActor(position, stage, this.dimensions, "paint_icon.png")

        override fun paint(g: Graphics2D) {
            g.color = currColor
            g.fillRect(1, 0, this.dimensions.width-1, this.dimensions.height)
            backing.paint(g)

            if (isHovered) {
                g.color = Color(0, 0, 0, 64)
                g.fillRect(1, 0, this.dimensions.width - 1, this.dimensions.height)
            }
        }
    }

    val Text = object: ToggleableButtonActor(dimensions.getWidth() - BUTTON_DIMENSIONS - 1 to BUTTON_DIMENSIONS.toDouble(), this, Dimension(BUTTON_DIMENSIONS, BUTTON_DIMENSIONS), "", currColor, {
        if (currAction != Actions.TEXT) {
            currAction = Actions.TEXT
        } else {
            currAction = Actions.NONE
        }
    }) {
        val backing = ImageBoundActor(position, stage, this.dimensions, "text.png")

        override fun paint(g: Graphics2D) {
            backing.paint(g)

            if (isHovered || currAction == Actions.TEXT) {
                g.color = Color(0, 0, 0, 127)
                g.fillRect(1, 0, this.dimensions.width - 1, this.dimensions.height)
            }
        }
    }

    val Pencil = object: ToggleableButtonActor(dimensions.getWidth() - BUTTON_DIMENSIONS - 1 to BUTTON_DIMENSIONS*2.0, this, Dimension(BUTTON_DIMENSIONS, BUTTON_DIMENSIONS), "", currColor, {
        if (currAction != Actions.PAINT) {
            currAction = Actions.PAINT
        } else {
            currAction = Actions.NONE
        }
    }) {

        val backing = ImageBoundActor(position, stage, this.dimensions, "pencil.png")

        override fun paint(g: Graphics2D) {
            g.color = currColor
            g.fillRect(1, 0, this.dimensions.width-1, this.dimensions.height)
            backing.paint(g)

            if (isHovered || currAction == Actions.PAINT) {
                g.color = Color(0, 0, 0, 127)
                g.fillRect(1, 0, this.dimensions.width - 1, this.dimensions.height)
            }
        }
    }

    val Eraser = object: ToggleableButtonActor(dimensions.getWidth() - BUTTON_DIMENSIONS - 1 to BUTTON_DIMENSIONS*3.0, this, Dimension(BUTTON_DIMENSIONS, BUTTON_DIMENSIONS), "", currColor, {
        if (currAction != Actions.ERASE) {
            currAction = Actions.ERASE
        } else {
            currAction = Actions.NONE
        }
    }) {
        val backing = ImageBoundActor(position, stage, this.dimensions, "eraser.png")

        override fun paint(g: Graphics2D) {
            backing.paint(g)

            if (isHovered || currAction == Actions.ERASE) {
                g.color = Color(0, 0, 0, 127)
                g.fillRect(1, 0, this.dimensions.width - 1, this.dimensions.height)
            }
        }
    }

    val GhostActor = object: Actor(0.0 to 0.0, this, dimensions) {

        @Volatile var lastX = 0
        @Volatile var lastY = 0

        override fun paint(g: Graphics2D) {
            g.color = Color.BLACK
            if (currAction == Actions.PAINT) {
                g.drawOval(lastX - PIXEL_SIZE / 2, lastY - PIXEL_SIZE / 2, PIXEL_SIZE, PIXEL_SIZE)
            } else if (currAction == Actions.ERASE) {
                g.drawRect(lastX - (PIXEL_SIZE+1) / 2, lastY - (PIXEL_SIZE+1) / 2, PIXEL_SIZE+1, PIXEL_SIZE+1)
            }
        }

        val isMousePressed: Boolean
            get() = isSelected

        override fun onHover(coords: Pair<Int, Int>) {
            val (currX, currY) = coords
            lastX = currX
            lastY = currY

            if (isMousePressed) {
                if (currAction == Actions.ERASE)
                    collectActorsAt(coords.map(true)).forEach { actors.remove(it) }
                else if (currAction == Actions.PAINT)
                    actors.add(actors.size - 5, Pixel(coords.map(), currColor, PIXEL_SIZE))
            }
        }

        override fun onClick(mouseNumber: Int, coords: Pair<Int, Int>) {
            val (currX, currY) = coords
            lastX = currX
            lastY = currY

            if (actors.stream().filter { it is ButtonActor }.filter { coords.first <= it.position.first + it.dimensions.width && coords.first >= it.position.first && coords.second <= it.position.second + it.dimensions.height && coords.second >= it.position.second }.count() < 1 ) {
                if (currAction == Actions.ERASE)
                    collectActorsAt(coords.map(true)).forEach { actors.remove(it) }
                else if (currAction == Actions.PAINT)
                    actors.add(actors.size - 5, Pixel(coords.map(), currColor, PIXEL_SIZE))
                else if (currAction == Actions.TEXT)
                    actors.add(actors.size - 5, TextBox(coords.map()))
            }
        }

        override fun onScroll(scrollAmount: Int, coords: Pair<Int, Int>) {
            PIXEL_SIZE = Math.max(0, PIXEL_SIZE - scrollAmount)
        }

        fun collectActorsAt(coords: Pair<Double, Double>): Collection<Actor> = actors.stream().filter {
            return@filter (it is Pixel && coords.first <= it.position.first + PIXEL_SIZE + 1 && coords.first >= it.position.first && coords.second <= it.position.second + PIXEL_SIZE + 1 && coords.second >= it.position.second)
                    || (it is TextBox && coords.first <= it.position.first + it.width && coords.first >= it.position.first && coords.second <= it.position.second + it.height && coords.second >= it.position.second)
        }.collect(Collectors.toSet())

        fun Pair<Int, Int>.map(isErasing: Boolean = false): Pair<Double, Double> = (this.first.toDouble() - (if (isErasing) -(PIXEL_SIZE / 2) else (PIXEL_SIZE / 2))) / 2 to (this.second.toDouble() - (if (isErasing) -(PIXEL_SIZE / 2) else (PIXEL_SIZE / 2))) / 2
    }

    inner class Pixel(position: Pair<Double, Double>, val color: Color, val size: Int) : Actor(position, this, Dimension(size, size)) {

        override fun paint(g: Graphics2D) {
            g.color = color
            g.fillOval(position.first.toInt(), position.second.toInt(), size, size)
        }
    }

    inner class TextBox(position: Pair<Double, Double>) : Actor(position, this, dimensions) {

        var text: String
        var width: Int = 0
        var height: Int = 0

        init {
            val chooser = JFormattedTextField()
            val dialog = JDialog(scene.window.frame, localization["gui.text"], Dialog.ModalityType.APPLICATION_MODAL)
            dialog.add(chooser)
            dialog.pack()
            dialog.setLocationRelativeTo(null)
            dialog.isVisible = true
            text = chooser.text
        }

        override fun paint(g: Graphics2D) {
            if (width == 0 && height == 0) {
                width = g.fontMetrics.stringWidth(text)
                height = g.fontMetrics.height
            }

            g.color = Color.BLACK
            g.drawRect(position.first.toInt() - 2, position.second.toInt() + 3, width + 2, height + 2)
            g.translate(0, height)
            g.drawString(text, position.first.toInt(), position.second.toInt())
        }
    }

    override val actors: MutableList<Actor> = CopyOnWriteArrayList<Actor>(arrayOf(this@PaintingStage.GhostActor, PaintSelector, Text, Pencil, Eraser))

    override fun paint(g: Graphics2D) {
        g.color = Color.BLACK
        g.drawRect(0, 0, dimensions.width, dimensions.height)
    }

    enum class Actions {
        NONE, TEXT, PAINT, ERASE
    }
}