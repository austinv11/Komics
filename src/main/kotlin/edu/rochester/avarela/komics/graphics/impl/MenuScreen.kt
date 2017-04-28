package edu.rochester.avarela.komics.graphics.impl

import edu.rochester.avarela.komics.`class`.Profile
import edu.rochester.avarela.komics.activities.*
import edu.rochester.avarela.komics.centerText
import edu.rochester.avarela.komics.graphics.Actor
import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import edu.rochester.avarela.komics.graphics.Window
import edu.rochester.avarela.komics.graphics.prebuilt.ButtonActor
import edu.rochester.avarela.komics.graphics.prebuilt.ImageBoundActor
import edu.rochester.avarela.komics.lang.Languages
import edu.rochester.avarela.komics.localization
import edu.rochester.avarela.komics.profile
import java.awt.*
import java.io.File
import javax.swing.*
import javax.swing.JLabel
import javax.swing.GroupLayout
import kotlin.system.exitProcess

class MenuScene(window: Window) : Scene(window) {

    init {
        val profileDir = File("./profiles")
        if (!profileDir.exists())
            profileDir.mkdir()

        val profiles = Profile.profiles

        if (profiles.isEmpty()) {
            profile = CreateProfileFrame(window.frame).profile
        } else {
            profile = ProfileSelectorFrame(window.frame).profile
        }

        window.frame.requestFocus()
        window.frame.contentPane.requestFocus()
    }

    override val background: Color = Color.LIGHT_GRAY
    override val stages: List<Stage> = listOf(ToolbarStage(this, Dimension(dimensions.width, (.05 * dimensions.height).toInt())),
            AccountNameStage(this, Dimension(160, 30)),
            ActivityStage(this, .1 * dimensions.height to .1 * dimensions.height, Dimension((.9 * dimensions.width).toInt(), (.8 * dimensions.height).toInt())),
            LogoStage(this))

    class AssignmentStage(scene: Scene, position: Pair<Double, Double>, dimensions: Dimension) : Stage(scene, position, dimensions) { //TODO
        override val actors: List<Actor>
            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    }

    class ActivityStage(scene: Scene, position: Pair<Double, Double>, dimensions: Dimension) : Stage(scene, position, dimensions) {
        override val actors: List<Actor> by lazy {
            var buttons = listOf<ActivityButton>()

            val buttonDimensions = Dimension((dimensions.width - (2 * PADDING)) / 3, (dimensions.height - PADDING) / 2)

            buttons += ActivityButton(0.0 to 0.0, buttonDimensions, localization["gui.activity.title"], MakeATitleActivity(window))
            buttons += ActivityButton((buttonDimensions.getWidth() + PADDING) to 0.0, buttonDimensions, localization["gui.activity.match"], MatchCaptionsActivity(window))
            buttons += ActivityButton((buttonDimensions.getWidth() + PADDING) * 2 to 0.0, buttonDimensions, localization["gui.activity.reorder"], ReorderActivity(window))

            buttons += ActivityButton(0.0 to (buttonDimensions.getHeight() + PADDING), buttonDimensions, localization["gui.activity.experiment"], ExperimentActivity(window))
            buttons += ActivityButton((buttonDimensions.getWidth() + PADDING) to (buttonDimensions.getHeight() + PADDING), buttonDimensions, localization["gui.activity.fill"], FillThePanelActivity(window))
            buttons += ActivityButton((buttonDimensions.getWidth() + PADDING) * 2 to (buttonDimensions.getHeight() + PADDING), buttonDimensions, localization["gui.activity.custom"], CustomComicsActivity(window))

            return@lazy buttons
        }

        inner class ActivityButton(position: Pair<Double, Double>, dimensions: Dimension, text: String, val activity: Scene) : ButtonActor(position, this, dimensions, text, Color.PINK) {
            override fun onButtonPress() {
                window.scene = activity
            }
        }

        companion object {
            const val PADDING = 25
        }
    }

    class ToolbarStage(scene: Scene, dimensions: Dimension) : Stage(scene, 0.0 to 0.0, dimensions) {
        override val actors: List<Actor> by lazy {
            val width = dimensions.getWidth() / BUTTON_COUNT

            val aboutButton = object: ButtonActor(0*width to 0.0, this, Dimension(width.toInt(), dimensions.height), localization["gui.about"], Color.BLUE) {
                override fun onButtonPress() {
                    window.scene = AboutScene(scene as MenuScene)
                }
            }

            val logOutButton = object: ButtonActor(1*width to 0.0, this, Dimension(width.toInt(), dimensions.height), localization["gui.log_out"], Color.BLUE) {
                override fun onButtonPress() {
                    window.scene = MenuScene(window)
                }
            }

            val quitButton = object: ButtonActor(2*width to 0.0, this, Dimension(width.toInt(), dimensions.height), localization["gui.quit"], Color.BLUE) {
                override fun onButtonPress() {
                    exitProcess(1)
                }
            }

            return@lazy listOf<Actor>(aboutButton, logOutButton, quitButton)
        }

        companion object {
            const val BUTTON_COUNT = 3.0
        }
    }

    class AccountNameStage(scene: Scene, dimensions: Dimension) : Stage(scene, 0.0 to scene.dimensions.getHeight() - dimensions.getHeight(), dimensions) {
        override val actors: List<Actor> = listOf(object: Actor(0.0 to 0.0, this, dimensions) {
            override fun paint(g: Graphics2D) {
                g.color = Color.BLACK
                g.centerText(localization["gui.welcome"].format(profile!!.name), dimensions.getWidth().toFloat() / 2F, dimensions.getHeight().toFloat() / 2F)
            }
        })
    }

    class LogoStage(scene: Scene) : Stage(scene, -6.0 to scene.dimensions.getHeight() / 4, Dimension(580/2, 225/2)) {
        override val actors: List<Actor> = listOf(object: ImageBoundActor(0.0 to 0.0, this, dimensions, "komics.png") {
            override fun paint(g: Graphics2D) {
                g.rotate(-Math.PI / 6.0)
                super.paint(g)
            }
        })
    }
}

class CreateProfileFrame(frame: JFrame) : JDialog(frame, "Create Profile", ModalityType.APPLICATION_MODAL) {

    init {
        this.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        this.setLocationRelativeTo(null)
        val layout = GroupLayout(this.contentPane)
        contentPane.setLayout(layout)
        layout.autoCreateGaps = true
        val hGroup = layout.createSequentialGroup()
        val yLabelGroup = layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
        hGroup.addGroup(yLabelGroup)
        val yFieldGroup = layout.createParallelGroup()
        hGroup.addGroup(yFieldGroup)
        layout.setHorizontalGroup(hGroup)
        val vGroup = layout.createSequentialGroup()
        layout.setVerticalGroup(vGroup)
        val p = GroupLayout.PREFERRED_SIZE
        val nameField = JTextField(8)
        nameField.addPropertyChangeListener {
            profileName = (it.source as JTextField).text
        }
        val nativeLang = JComboBox<String>(arrayOf("") + Languages.CODES.values.toTypedArray())
        nativeLang.addActionListener {
            val selected = (it.source as JComboBox<String>).selectedItem as String
            this.nativeLang = Languages.CODES.filter { it.value == selected }.keys.first()!!
        }
        val learningLang = JComboBox<String>(arrayOf("") + Languages.CODES.values.toTypedArray())
        learningLang.addActionListener {
            val selected = (it.source as JComboBox<String>).selectedItem as String
            this.learningLang = Languages.CODES.filter { it.value == selected }.keys.first()!!
        }
        val labels = arrayOf<Component>(JLabel("Name:"), JLabel("Native Language:"), JLabel("Language to Learn:"), JLabel())
        val fields = arrayOf<Component>(nameField, nativeLang, learningLang, JButton("Enter").apply { addActionListener { this@CreateProfileFrame.dispose() } })
        for (label in labels) {
            yLabelGroup.addComponent(label)
        }
        for (field in fields) {
            yFieldGroup.addComponent(field, p, p, p)
        }
        for (ii in 0..labels.size - 1) {
            vGroup.addGroup(layout.createParallelGroup().addComponent(labels[ii]).addComponent(fields[ii], p, p, p))
        }
        this.pack()
        this.isResizable = false
        this.isVisible = true
    }

    private lateinit var profileName: String
    private lateinit var nativeLang: String
    private lateinit var learningLang: String

    val profile: Profile
        get() {
            val pro = Profile(profileName, nativeLang, learningLang, arrayOf(), 0F)
            pro.save()
            return pro
        }
}

class ProfileSelectorFrame(frame: JFrame) : JDialog(frame, "Select Profile", ModalityType.APPLICATION_MODAL) {

    init {
        this.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        this.setLocationRelativeTo(null)
        val layout = BoxLayout(this.contentPane, BoxLayout.Y_AXIS)
        contentPane.setLayout(layout)
        val profiles = JComboBox<String>(arrayOf("Select Profile") + Profile.profiles.map(Profile::name).toTypedArray())
        profiles.addActionListener {
            val selected = (it.source as JComboBox<String>).selectedItem as String
            this.profile = Profile.profiles.find { it.name == selected }!!
        }
        add(profiles)
        val buttonPanel = JPanel()
        buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.X_AXIS)
        val ok = JButton("Ok").apply { addActionListener { this@ProfileSelectorFrame.dispose() } }
        buttonPanel.add(ok)
        val create = JButton("Create Profile").apply { addActionListener { profiles.addItem(CreateProfileFrame(frame).profile.name) } }
        buttonPanel.add(create)
        add(buttonPanel)
        pack()
        isResizable = false
        isVisible = true
    }

    lateinit var profile: Profile
}