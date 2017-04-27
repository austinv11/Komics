package edu.rochester.avarela.komics.graphics.prebuilt

import edu.rochester.avarela.komics.graphics.Actor
import edu.rochester.avarela.komics.graphics.Stage
import java.awt.*
import java.awt.geom.Rectangle2D

abstract class HoverableActor(position: Pair<Double, Double>,
                              stage: Stage, dimensions: Dimension,
                              val extensionFactor: Float = 1.2F) : Actor(position, stage, dimensions) {

    override fun paint(g: Graphics2D) {
        if (isHovered) {
            val paintableG = g.create() as Graphics2D
            val paint = RadialGradientPaint(Rectangle2D.Double(position.first, position.second, dimensions.getWidth(), dimensions.getHeight()),
                    floatArrayOf(.2F, .8F), arrayOf(Color.WHITE, Color.BLACK), MultipleGradientPaint.CycleMethod.NO_CYCLE)
            paintableG.paint = paint
            paintableG.fillRect(position.first.toInt() + dimensions.width - (dimensions.getWidth() * extensionFactor / 2).toInt(), position.second.toInt() + dimensions.height - (dimensions.getHeight() * extensionFactor).toInt(), (dimensions.getWidth() * extensionFactor).toInt(), (dimensions.getHeight() * extensionFactor).toInt())
        }
    }
}