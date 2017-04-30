package edu.rochester.avarela.komics.graphics.prebuilt

import edu.rochester.avarela.komics.graphics.Actor
import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import java.awt.Dimension

class PanelStage(scene: Scene, position: Pair<Double, Double>, dimensions: Dimension) : Stage(scene, position, dimensions) {

    override val actors: List<Actor> = listOf()
}