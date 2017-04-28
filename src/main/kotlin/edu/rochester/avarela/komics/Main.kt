package edu.rochester.avarela.komics

import com.google.gson.GsonBuilder
import edu.rochester.avarela.komics.`class`.Profile
import edu.rochester.avarela.komics.graphics.Window
import edu.rochester.avarela.komics.graphics.impl.LoadScene
import edu.rochester.avarela.komics.lang.Language
import edu.rochester.avarela.komics.lang.Languages
import java.awt.Dimension
import java.awt.Graphics2D
import java.io.File
import javax.swing.UIManager
import org.reflections.scanners.ResourcesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.Reflections
import java.util.regex.Pattern
import kotlin.concurrent.thread


fun main(args: Array<String>) {
    thread { localization }

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    Window("Komics", Dimension(900, 600)) {
        scene = LoadScene(this)
    }
}

fun Graphics2D.centerText(string: String, x: Float, y: Float) {
    drawString(string, x - (fontMetrics.stringWidth(string).toFloat() / 2F), y)
}

const val IS_DEBUG_MODE: Boolean = true //TODO set false!!!

@Volatile var profile: Profile? = null

val localization: Language
    get() = Languages[profile?.localLang ?: "en_US"]
val target: Language
    get() = Languages[profile?.desiredLang ?: "en_US"]

val gson = GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().setLenient().create()

val Any.json: String
    get() = gson.toJson(this)

inline fun <reified T> String.obj(): T = gson.fromJson<T>(this, T::class.java)

val resources: List<File> by lazy {
    val reflections = Reflections(ConfigurationBuilder().setUrls(
            ClasspathHelper.forJavaClassPath()).setScanners(ResourcesScanner()))
    return@lazy reflections.getResources { true }.filter { it.startsWith("komics") }.map {
        val file = File.createTempFile(it.replaceAfter('.', ""), "." + it.split('.')[1])
        file.writeBytes(ClassLoader.getSystemClassLoader().getResourceAsStream(it).readBytes())
        return@map file
    }
}

val tempPatter = Pattern.compile("\\.+\\d+").toRegex()
fun String.stripTemp(): String = this.replaceFirst(tempPatter, "")