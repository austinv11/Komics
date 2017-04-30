package edu.rochester.avarela.komics.graphics.prebuilt

import edu.rochester.avarela.komics.SpecialChars
import edu.rochester.avarela.komics.centerText
import edu.rochester.avarela.komics.graphics.Actor
import edu.rochester.avarela.komics.graphics.Stage
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D

abstract class ButtonActor(position: Pair<Double, Double>,
                           stage: Stage,
                           dimensions: Dimension,
                           var text: String,
                           var defaultColor: Color,
                           var highlightColor: Color = Color.BLACK,
                           var hoveredColor: Color = defaultColor.darker()) : Actor(position, stage, dimensions) {

    override fun paint(g: Graphics2D) {
        g.color = if (isHovered) hoveredColor else defaultColor
        g.fillRect(0, 0, dimensions.width, dimensions.height)

        g.color = highlightColor
        g.drawRect(0, 0, dimensions.width, dimensions.height)

        g.color = Color.WHITE
        g.centerText(text, dimensions.getWidth().toFloat() / 2F, dimensions.getHeight().toFloat() / 2F)
    }

    override fun onClick(mouseNumber: Int, coords: Pair<Int, Int>) {
        onButtonPress()
    }

    override fun onKeyInput(char: Char) {
        super.onKeyInput(char)

        if (char == SpecialChars.ENTER && isSelected)
            onButtonPress()
    }

    abstract fun onButtonPress()
}