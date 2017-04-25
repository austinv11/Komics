package edu.rochester.avarela.komics

import com.google.gson.GsonBuilder
import edu.rochester.avarela.komics.`class`.Profile
import edu.rochester.avarela.komics.graphics.Window
import edu.rochester.avarela.komics.graphics.impl.LoadScene
import edu.rochester.avarela.komics.lang.Language
import edu.rochester.avarela.komics.lang.Languages
import edu.rochester.avarela.komics.store.Manifest
import java.awt.Dimension
import java.awt.Graphics2D
import java.io.File
import java.time.LocalDateTime
import javax.swing.UIManager

fun main(args: Array<String>) {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    Window("Komics", Dimension(900, 600)) {
        scene = LoadScene(this)
    }
}

fun Graphics2D.centerText(string: String, x: Float, y: Float) {
    drawString(string, x - (fontMetrics.stringWidth(string).toFloat() / 2F), y - (fontMetrics.height.toFloat() / 2F))
}

const val BASE_URL: String = "https://github.com/austinv11/Komics/raw/master/"
const val IS_DEBUG_MODE: Boolean = true //TODO set false!!!

var profile: Profile? = null

val localization: Language
    get() = Languages[profile?.localLang ?: "en_US"]
val target: Language
    get() = Languages[profile?.desiredLang ?: "en_US"]

val gson = GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().setLenient().create()

val Any.json: String
    get() = gson.toJson(this)

inline fun <reified T> String.obj(): T = gson.fromJson<T>(this, T::class.java)