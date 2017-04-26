package edu.rochester.avarela.komics.graphics

import edu.rochester.avarela.komics.SpecialChars
import edu.rochester.avarela.komics.graphics.impl.LoadScene
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.*
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.Timer
import javax.swing.WindowConstants

import java.awt.event.KeyEvent.*

class Window(val name: String, val dimensions: Dimension, val initializer: Window.() -> Unit) : ActionListener, MouseListener, MouseMotionListener, KeyListener {

    val KeyEvent.char: Char
        get() {
            if (this.isActionKey) {
                return when (this.keyCode) {
                    VK_UP, VK_KP_UP, VK_PAGE_UP -> SpecialChars.UP
                    VK_DOWN, VK_KP_DOWN, VK_PAGE_DOWN -> SpecialChars.DOWN
                    VK_LEFT, VK_KP_LEFT -> SpecialChars.LEFT
                    VK_RIGHT, VK_KP_RIGHT -> SpecialChars.RIGHT
                    VK_ENTER -> SpecialChars.ENTER
                    VK_SHIFT -> SpecialChars.SHIFT
                    VK_CAPS_LOCK -> SpecialChars.CAPS_LOCK
                    VK_TAB -> SpecialChars.TAB
                    VK_BACK_SPACE -> SpecialChars.BACKSPACE
                    VK_CONTROL -> SpecialChars.CTRL
                    VK_ALT -> SpecialChars.ALT
                    else -> '\u0000' //NULL char because we don't care for this special key
                }
            } else {
                return this.keyChar
            }
        }

    fun resolveKeyListeningActors(): List<Actor> = scene?.stages?.flatMap { it.actors }
            ?.filter { !it.keyInputRequiresSelection || (it.keyInputRequiresSelection && it.isSelected) }?.toList()
            ?: listOf()

    fun resolveMouseListeningActors(e: MouseEvent): List<Actor> = scene?.stages?.flatMap { it.actors }?.filter {
        val transformed = it.transformAbsoluteCoords(e.x to e.y)
        return@filter transformed.first >= 0 && transformed.second >= 0 && transformed.first < it.dimensions.width && transformed.second < it.dimensions.height
    }?.toList() ?: listOf()

    fun Actor.transformAbsoluteCoords(originalCoords: Pair<Int, Int>): Pair<Int, Int> {
        var (x, y) = originalCoords
        val (sX, sY) = component.scaleFactor

        //Scale the coords
        x = (x.toDouble() * sX).toInt()
        y = (y.toDouble() * sY).toInt()

        //Now start displacing the relative coords
        val (dX1, dY1) = stage.position
        x -= dX1.toInt()
        y -= dY1.toInt()

        val (dX2, dY2) = position
        x -= dX2.toInt()
        y -= dY2.toInt()

        return x to y
    }

    override fun keyTyped(e: KeyEvent) {
        resolveKeyListeningActors().forEach { it.onKeyInput(e.char) }
    }

    override fun keyPressed(e: KeyEvent) {
        resolveKeyListeningActors().forEach { it.onKeyPressed(e.char) }
    }

    override fun keyReleased(e: KeyEvent) {
        resolveKeyListeningActors().forEach { it.onKeyReleased(e.char) }
    }

    override fun mouseReleased(e: MouseEvent) {
        resolveMouseListeningActors(e).forEach { it.onMouseReleased(e.button, it.transformAbsoluteCoords(e.x to e.y)) }
    }

    override fun mouseEntered(e: MouseEvent) {}

    override fun mouseClicked(e: MouseEvent) {
        resolveMouseListeningActors(e).forEach { it.onClick(e.button, it.transformAbsoluteCoords(e.x to e.y)) }
    }

    override fun mouseExited(e: MouseEvent) {}

    override fun mousePressed(e: MouseEvent) {
        resolveMouseListeningActors(e).forEach { it.onMousePressed(e.button, it.transformAbsoluteCoords(e.x to e.y)) }
    }

    override fun mouseMoved(e: MouseEvent) {
        val hovered = resolveMouseListeningActors(e).map { it.onHover(it.transformAbsoluteCoords(e.x to e.y)); return@map it }.toList()
        scene?.stages?.flatMap { it.actors }?.filter { !hovered.contains(it) && it.isHovered }?.forEach { it.onHoverExit() }
    }

    override fun mouseDragged(e: MouseEvent) {
        //TODO maybe handle dragging?
    }

    var scene: Scene? = null
    private var lastTime = System.currentTimeMillis()

    override fun actionPerformed(e: ActionEvent) {
        val newTime = System.currentTimeMillis()
        val dT = newTime - lastTime
        lastTime = newTime

        scene?.onUpdate(dT)

        frame.repaint()
    }

    val frame = JFrame(name)
    val component = WindowComponent()

    init {
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.size = dimensions
        frame.isVisible = true
        frame.setLocationRelativeTo(null)
        frame.add(component)
        Timer(20, this).start()
        frame.addMouseListener(this)
        frame.addKeyListener(this)
        frame.isFocusable = true
        frame.requestFocus()

        initializer(this)
    }

    inner class WindowComponent : JComponent() {

        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            val g2D = g as Graphics2D
            val (sX, sY) = scaleFactor
            g2D.scale(sX, sY)
            scene?.draw(g2D)
        }

        val scaleFactor: Pair<Double, Double>
            get() = frame.contentPane.size.width.toDouble() / dimensions.width.toDouble() to frame.contentPane.size.height.toDouble() / dimensions.height.toDouble()
    }
}