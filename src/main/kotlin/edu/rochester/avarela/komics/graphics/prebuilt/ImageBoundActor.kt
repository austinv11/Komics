package edu.rochester.avarela.komics.graphics.prebuilt

import edu.rochester.avarela.komics.graphics.Actor
import edu.rochester.avarela.komics.graphics.Stage
import edu.rochester.avarela.komics.resources
import edu.rochester.avarela.komics.stripTemp
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Image
import javax.imageio.ImageIO
import javax.swing.ImageIcon

open class ImageBoundActor(position: Pair<Double, Double>,
                           stage: Stage,
                           dimensions: Dimension,
                           file: String) : Actor(position, stage, dimensions) {

    val image by lazy {
        ImageIcon(ImageIO.read(resources.find { it.name.stripTemp() == file }).getScaledInstance(dimensions.width, dimensions.height, Image.SCALE_AREA_AVERAGING))
    }

    override fun paint(g: Graphics2D) {
        g.drawImage(image.image, position.first.toInt(), position.second.toInt(), image.imageObserver)
    }
}