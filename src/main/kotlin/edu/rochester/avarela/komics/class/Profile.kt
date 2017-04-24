package edu.rochester.avarela.komics.`class`

import edu.rochester.avarela.komics.json
import edu.rochester.avarela.komics.obj
import java.io.File

data class Profile(val name: String,
                   var localLang: String = "en_US",
                   var desiredLang: String = "fr_FR",
                   var assignments: Array<Assignment>,
                   var progress: Float) {

    fun save() {
        File("./profiles/${name.replace(" ", "")}.json").writeText(this.json)
    }

    companion object {

        fun load(file: File): Profile = file.readText().obj()

        val profiles: MutableList<Profile>
            get() = File("./profiles").listFiles().map(this::load).toMutableList()
    }
}