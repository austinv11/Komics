package edu.rochester.avarela.komics.graphics.impl

import edu.rochester.avarela.komics.`class`.Profile
import edu.rochester.avarela.komics.graphics.Scene
import edu.rochester.avarela.komics.graphics.Stage
import edu.rochester.avarela.komics.graphics.Window
import edu.rochester.avarela.komics.lang.Languages
import java.awt.Color
import java.awt.Component
import java.io.File
import javax.swing.*
import javax.swing.JLabel
import javax.swing.GroupLayout

class MenuScene(window: Window) : Scene(window) {

    val profile: Profile

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
    }

    override val background: Color = Color.LIGHT_GRAY
    override val stages: List<Stage> = listOf()
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
        val nativeLang = JComboBox<String>(Languages.CODES.values.toTypedArray())
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
    private var nativeLang: String = "en_US"
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