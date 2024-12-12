package action

import com.google.common.base.CaseFormat
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.Messages
import helper.DataService
import helper.GNWFileName
import java.io.*
import java.util.*
import kotlin.collections.HashMap


class NewGNWAction : AnAction() {
    private var project: Project? = null
    private lateinit var psiPath: String
    private var data = DataService.instance

    /**
     * module name
     */
    private lateinit var moduleName: String


    override fun actionPerformed(event: AnActionEvent) {
        project = event.project
        psiPath = event.getData(PlatformDataKeys.PSI_ELEMENT).toString()
        psiPath = psiPath.substring(psiPath.indexOf(":") + 1)
        initView()
    }

    private fun initView() {
        NewGetXView(object : GetXListener {
            override fun onSave(): Boolean {
                return save()
            }

            override fun onDataChange(view: NewGetXView) {
                //module name
                moduleName = view.nameTextField.text

                //deal default value
                val modelType = view.modeGroup.selection.actionCommand

                data.Controller = (GNWFileName.Controller == modelType)
                data.ListView = (GNWFileName.ListView == modelType)
                data.FetchApi = (GNWFileName.FetchApi == modelType)
            }
        })
    }

    /**
     * generate  file
     */
    private fun save(): Boolean {
        if ("" == moduleName.trim { it <= ' ' }) {
            Messages.showInfoMessage(project, "Please input the module name", "Info")
            return false
        }
        //Create a file
        createFile()
        //Refresh project
        project?.guessProjectDir()?.refresh(false, true)

        return true
    }

    private fun createFile() {
        val prefix = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, upperCase(moduleName))
        var folder = ""
        var prefixName = "${prefix}_"

        //select generate file mode
        val path = psiPath + folder
        if (data.ListView){
            generateViewFile(path, prefixName)
        }
        if(data.Controller){
            generateController(path, prefixName)
        }
        if(data.FetchApi){
            generateFetchApi(path, prefixName)
        }
    }

    private fun generateViewFile(path: String, prefixName: String) {
        generateFile(
            "ListView.dart", path, "${prefixName}${data.list.ListViewName.lowercase(Locale.getDefault())}.dart"
        )
    }
    private fun generateController(path: String, prefixName: String) {
        generateFile(
             "Controller.dart", path,"${prefixName}${data.controller.ControllerName.lowercase(Locale.getDefault())}.dart"
        )
    }
    private fun generateFetchApi(path: String, prefixName: String) {
        generateFile(
            "api_fetch_data.dart", path,"api_fetch_data.dart"
        )
    }

    private fun generateFile(inputFileName: String, filePath: String, outFileName: String) {
        //content deal
        val content = dealContent(inputFileName)

        //Write file
        try {
            val folder = File(filePath)
            // if file not exists, then create it
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val file = File("$filePath/$outFileName")
            if (!file.exists()) {
                file.createNewFile()
            }
            val fw = FileWriter(file.absoluteFile)
            val bw = BufferedWriter(fw)
            bw.write(content)
            bw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private var replaceContentMap = HashMap<String, String>()

    //content need deal
    private fun dealContent(inputFileName: String): String {
        //module name
        val name = upperCase(moduleName)
        //Adding a prefix requires modifying the imported class name
        var prefixName = "${CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)}_"


        //select suitable file, return suitable content

        var content = getSuitableContentFile(inputFileName)
        replaceContentMap.clear()

        //region GNW ListView
        replaceListView(inputFileName,prefixName)
        //endregion

        replaceContentMap["@name"] = name

        replaceContentMap.forEach { (key, value) ->
            content = content.replace(key.toRegex(), value)
        }

        return content
    }


    private fun replaceListView(inputFileName: String, prefixName: String) {
        if (!inputFileName.contains("ListView.dart")) {
            return
        }
        replaceContentMap["ListView.dart"] = "$prefixName${data.list.ListViewName.lowercase(Locale.getDefault())}.dart"
    }


    //region File View...
    private fun getSuitableContentFile(inputFileName: String): String {
        var defaultFolder = ""
        if(inputFileName.contains("ListView.dart")) {
            defaultFolder = "/templates/ListView"
        }
        if (inputFileName.contains("Controller.dart")) {
            defaultFolder = "/templates/Controller"
        }
        if(inputFileName.contains("api_fetch_data.dart")) {
            defaultFolder = "/templates/Api"
        }
    var content = ""
    try {
        val input = this.javaClass.getResourceAsStream("$defaultFolder/$inputFileName")
        content = String(readStream(input!!))
    } catch (e: Exception) {
        //some error
    }
    return content
    }
    //endregion


    @Throws(Exception::class)
    private fun readStream(inStream: InputStream): ByteArray {
        val outSteam = ByteArrayOutputStream()
        try {
            val buffer = ByteArray(1024)
            var len: Int
            while (inStream.read(buffer).apply { len = this } != -1) {
                outSteam.write(buffer, 0, len)
                println(String(buffer))
            }
        } catch (_: IOException) {
        } finally {
            outSteam.close()
            inStream.close()
        }
        return outSteam.toByteArray()
    }

    private fun upperCase(str: String): String {
        return str.substring(0, 1).uppercase(Locale.getDefault()) + str.substring(1)
    }
}
