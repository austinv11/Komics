package edu.rochester.avarela.komics.lang

import com.google.common.base.Predicate
import edu.rochester.avarela.komics.GAME
import org.reflections.Reflections

private val ASSET_LOADER by lazy { GAME.assetLoader!! }
private val LANGUAGE_MAP = mutableMapOf<String, MutableMap<String, String>>()

object Languages {

    init {
        Reflections.collect("edu.rochester", Predicate { it?.endsWith(".lang") ?: false }).getResources { it?.endsWith(".lang") ?: false } .forEach {
            println(it)
            val langCode = it.split(".")[0]
            LANGUAGE_MAP[langCode] = mutableMapOf()
            val map = LANGUAGE_MAP[langCode]
            ASSET_LOADER.loadText(it).forEach {
                print(it)
                if (it.isNotBlank() && !it.startsWith("#")) {
                    val split = it.split("=")
                    map!![split[0]] = split[1]
                }
            }
        }
    }

    operator fun get(key: String): Language = Language(key)

    val languages: List<String>
        get() = LANGUAGE_MAP.keys.toList()
}

data class Language(val lang: String) : MutableMap<String, String> {

    override val size: Int = LANGUAGE_MAP[lang]?.size ?: -1

    override fun containsKey(key: String): Boolean = LANGUAGE_MAP[lang]?.containsKey(key) ?: false

    override fun containsValue(value: String): Boolean = LANGUAGE_MAP[lang]?.containsValue(value) ?: false

    override fun get(key: String): String? = LANGUAGE_MAP[lang]?.get(key)

    override fun isEmpty(): Boolean = size == 0

    override val entries: MutableSet<MutableMap.MutableEntry<String, String>> = LANGUAGE_MAP[lang]?.entries ?: mutableSetOf<MutableMap.MutableEntry<String, String>>()
    override val keys: MutableSet<String> = LANGUAGE_MAP[lang]?.keys ?: mutableSetOf()
    override val values: MutableCollection<String> = LANGUAGE_MAP[lang]?.values ?: mutableListOf()

    override fun clear() { LANGUAGE_MAP[lang]?.clear() }

    override fun put(key: String, value: String): String? = LANGUAGE_MAP[lang]?.put(key, value)

    override fun putAll(from: Map<out String, String>) {
        LANGUAGE_MAP[lang]?.putAll(from)
    }

    override fun remove(key: String): String? = LANGUAGE_MAP[lang]?.remove(key)
}