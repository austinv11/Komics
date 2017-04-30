package edu.rochester.avarela.komics.graphics.prebuilt

import edu.rochester.avarela.komics.graphics.Actor
import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import java.awt.Dimension

class ComicStage(scene: Scene,
                 position: Pair<Double, Double>,
                 dimensions: Dimension,
                 val image: String,
                 val text: MutableList<TextActor> = mutableListOf()) : Stage(scene, position, dimensions) {

    override val actors: List<Actor>
        get() = listOf(ImageBoundActor(0.0 to 0.0, this, dimensions, image)) + text
}