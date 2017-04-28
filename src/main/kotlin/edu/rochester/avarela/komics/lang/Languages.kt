package edu.rochester.avarela.komics.lang

import edu.rochester.avarela.komics.resources
import edu.rochester.avarela.komics.stripTemp

private val LANGUAGE_MAP = mutableMapOf<String, MutableMap<String, String>>()

object Languages {

    val CODES = mapOf("en_US" to "English", "fr_FR" to "Français") //TODO remember to update

    init {
        resources.filter { it.name.endsWith("lang") }.forEach {
            val langCode = it.nameWithoutExtension.stripTemp()
            LANGUAGE_MAP[langCode] = mutableMapOf()
            val map = LANGUAGE_MAP[langCode]
            it.readLines().forEach {
                if (it.isNotBlank() && !it.startsWith("#")) {
                    val split = it.split("=")
                    map!![split[0]] = split[1]
                }
            }
        }

        languages.forEach { if (!CODES.containsKey(it)) println("WARNING: Missing language name for $it!") }
    }

    operator fun get(key: String): Language = Language(key)

    val languages: List<String>
        get() = LANGUAGE_MAP.keys.toList()
}

data class Language(val lang: String) : Map<String, String> {

    override val size: Int = LANGUAGE_MAP[lang]?.size ?: -1

    override fun containsKey(key: String): Boolean = LANGUAGE_MAP[lang]?.containsKey(key) ?: false

    override fun containsValue(value: String): Boolean = LANGUAGE_MAP[lang]?.containsValue(value) ?: false

    override fun get(key: String): String = LANGUAGE_MAP[lang]?.get(key) ?: key.apply { println("WARNING: Missing translation for $this") }

    override fun isEmpty(): Boolean = size == 0

    override val entries: MutableSet<MutableMap.MutableEntry<String, String>> = LANGUAGE_MAP[lang]?.entries ?: mutableSetOf<MutableMap.MutableEntry<String, String>>()
    override val keys: MutableSet<String> = LANGUAGE_MAP[lang]?.keys ?: mutableSetOf()
    override val values: MutableCollection<String> = LANGUAGE_MAP[lang]?.values ?: mutableListOf()
}