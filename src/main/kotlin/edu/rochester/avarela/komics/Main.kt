package edu.rochester.avarela.komics

import edu.rochester.avarela.komics.graphics.Window
import edu.rochester.avarela.komics.graphics.impl.LoadScene
import edu.rochester.avarela.komics.lang.Language
import edu.rochester.avarela.komics.lang.Languages
import java.awt.Dimension
import java.awt.Graphics2D

fun main(args: Array<String>) {
    Window("Komics", Dimension(900, 600)) {
        scene = LoadScene(this)
    }
}

fun Graphics2D.centerText(string: String, x: Float, y: Float) {
    drawString(string, x - (fontMetrics.stringWidth(string).toFloat() / 2F), y - (fontMetrics.height.toFloat() / 2F))
}

var localization: Language = Languages["en_US"]
var target: Language = Languages["fr_FR"]