package action
import com.intellij.ui.JBColor
import helper.DataService
import helper.GNWFileName
import java.awt.Container
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*

open class NewGetXView(private val getXListener: GetXListener) {
    private val data = DataService.instance

    /**
     * Overall popup entity
     */
    private var jDialog: JDialog = JDialog(JFrame(), "GNW File Generator")
    lateinit var nameTextField: JTextField
    lateinit var modeGroup: ButtonGroup


    private val keyListener: KeyListener = object : KeyListener {
        override fun keyTyped(e: KeyEvent) {}

        override fun keyPressed(e: KeyEvent) {
            if (e.keyCode == KeyEvent.VK_ENTER) confirm()
            if (e.keyCode == KeyEvent.VK_ESCAPE) dispose()
        }

        override fun keyReleased(e: KeyEvent) {}
    }

    private val actionChangeListener = ActionListener {
        //data change
        getXListener.onDataChange(this)
        //click btn
        if (it.actionCommand == "Cancel") {
            dispose()
        } else if (it.actionCommand == "OK") {
            confirm()
        }
    }

    init {
        //Set function button
        val container = jDialog.contentPane
        container.layout = BoxLayout(container, BoxLayout.Y_AXIS)
        //Set the main module style: mode, function
        //deal default value
        setMode(container)
        //Generate module name and ok cancel button
        setModuleAndConfirm(container)
        //Choose a pop-up style
        setJDialog()
    }

    /**
     * Main module
     */
    private fun setMode(container: Container) {
        //Two rows and two columns
        val template = JPanel()
        template.layout = GridLayout(2, 2)
        //Set the main module style：mode, function
        template.border = BorderFactory.createTitledBorder("Select Mode")

        //controller code generator button
        val controllerBtn = JRadioButton(GNWFileName.Controller, data.Controller)
        controllerBtn.actionCommand = GNWFileName.Controller
        controllerBtn.addActionListener(actionChangeListener)
        controllerBtn.border = BorderFactory.createEmptyBorder(5, 10, 10, 100)

        //ListView code generator button
        val listViewBtn = JRadioButton(GNWFileName.ListView, data.ListView)
        listViewBtn.actionCommand = GNWFileName.ListView
        listViewBtn.addActionListener(actionChangeListener)
        listViewBtn.border = BorderFactory.createEmptyBorder(5, 10, 10, 100)

        //Api Fetch code generator button
        val apiFetchBtn = JRadioButton(GNWFileName.FetchApi, data.FetchApi)
        apiFetchBtn.actionCommand = GNWFileName.FetchApi
        apiFetchBtn.addActionListener(actionChangeListener)
        apiFetchBtn.border = BorderFactory.createEmptyBorder(5, 10, 10, 10)

        template.add(controllerBtn)
        template.add(listViewBtn)
        template.add(apiFetchBtn)

        modeGroup = ButtonGroup()
        modeGroup.add(controllerBtn)
        modeGroup.add(listViewBtn)
        modeGroup.add(apiFetchBtn)

        container.add(template)
        setSpacing(container)
    }

    /**
     * Generate file name and button
     */
    private fun setModuleAndConfirm(container: Container) {
        //input module name
        //Row：Box.createHorizontalBox() | Column：Box.createVerticalBox()
        //add Module Name
        val nameField = JPanel()
        val padding = JPanel()
        padding.border = BorderFactory.createEmptyBorder(0, 0, 5, 0)
        nameField.border = BorderFactory.createTitledBorder("Module Name")
        nameTextField = JTextField(33)
        nameTextField.addKeyListener(keyListener)
        padding.add(nameTextField)
        nameField.add(padding)
        container.add(nameField)

        //OK cancel button
        val cancel = JButton("Cancel")
        cancel.foreground = JBColor.RED
        cancel.addActionListener(actionChangeListener)
        val ok = JButton("OK")
        ok.foreground = JBColor.BLUE
        ok.addActionListener(actionChangeListener)
        val menu = JPanel()
        menu.layout = FlowLayout()
        menu.add(cancel)
        menu.add(ok)
        menu.border = BorderFactory.createEmptyBorder(10, 0, 10, 0)

        container.add(menu)
    }

    /**
     * Set the overall pop-up style
     */
    private fun setJDialog() {
        //The focus is on the current pop-up window,
        // and the focus will not shift even if you click on other areas
        jDialog.isModal = true
        //Set padding
        (jDialog.contentPane as JPanel).border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        //auto layout
        jDialog.pack()
        jDialog.setLocationRelativeTo(null)
        jDialog.isVisible = true
    }

    private fun setSpacing(container: Container) {
        val jPanel = JPanel()
        jPanel.border = BorderFactory.createEmptyBorder(0, 0, 3, 0)
        container.add(jPanel)
    }

    private fun confirm() {
        //data change, deal TextField listener
        getXListener.onDataChange(this)

        if (getXListener.onSave()) {
            dispose()
        }
    }

    private fun dispose() {
        jDialog.dispose()
    }
}

interface GetXListener {
    fun onSave(): Boolean

    fun onDataChange(view: NewGetXView)
}