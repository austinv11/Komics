package edu.rochester.avarela.komics.graphics

import edu.rochester.avarela.komics.graphics.impl.LoadScene
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.Timer
import javax.swing.WindowConstants

class Window(val name: String, val dimensions: Dimension, val initializer: Window.() -> Unit) : ActionListener {

    var scene: Scene? = null
    private var lastTime = System.currentTimeMillis()

    override fun actionPerformed(e: ActionEvent?) {
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

        initializer(this)
    }

    @Strictfp
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