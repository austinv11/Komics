package edu.rochester.avarela.komics.graphics.prebuilt

import edu.rochester.avarela.komics.graphics.Stage
import java.awt.Color
import java.awt.Dimension

open class ToggleableButtonActor(position: Pair<Double, Double>,
                                 stage: Stage,
                                 dimensions: Dimension,
                                 text: String,
                                 defaultColor: Color,
                                 var toggleListener: ToggleableButtonActor.() -> Unit,
                                 highlightColor: Color = Color.BLACK,
                                 hoveredColor: Color = defaultColor.darker()) : ButtonActor(position, stage, dimensions, text, defaultColor, highlightColor, hoveredColor) {

    var isToggled: Boolean = false

    override fun onButtonPress() {
        isToggled = !isToggled

        if (isToggled && !isHovered)
            isHovered = true

        toggleListener()
    }
}