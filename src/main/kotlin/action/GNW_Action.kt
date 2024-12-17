package action

import com.google.common.base.CaseFormat
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.Messages
import GNW_Helper.GNWDataService
import GNW_Helper.GNWFileName
import java.io.*
import java.util.*
import kotlin.collections.HashMap

class GNW_Action : AnAction() {
    private var project: Project? = null
    private lateinit var psiPath: String
    private var data = GNWDataService.instance

    private lateinit var moduleName: String

    override fun actionPerformed(event: AnActionEvent) {
        project = event.project
        psiPath = event.getData(PlatformDataKeys.PSI_ELEMENT).toString()
        psiPath = psiPath.substring(psiPath.indexOf(":") + 1)
        initView()
    }

    private fun initView() {
        GNW_View(object : GNWListener {
            override fun onSave(): Boolean {
                return save()
            }

            override fun onDataChange(view: GNW_View) {
                moduleName = view.nameTextField.text
                val modelType = view.modeGroup.selection.actionCommand

                data.Controller = (GNWFileName.Controller == modelType)
                data.ListView = (GNWFileName.ListView == modelType)
                data.ApiModule = (GNWFileName.ApiModule == modelType)
            }
        })
    }

    private fun save(): Boolean {
        if ("" == moduleName.trim { it <= ' ' }) {
            Messages.showInfoMessage(project, "Please enter the module name", "Alert")
            return false
        }
        createFile()
        project?.guessProjectDir()?.refresh(false, true)
        return true
    }

    private fun createFile() {
        var folder = ""
        val folderPath = "$psiPath/$moduleName"

        // Check if folder already exists
        if (File(folderPath).exists()) {
            Messages.showInfoMessage(project, "Folder already exists: $folderPath", "Alert")
            return
        }

        if (data.ApiModule) {
            folder = if (data.ApiModule) "$psiPath/$moduleName" else psiPath
            File(folder).mkdirs()
            generateApiModule(folder)
        }
        if(data.ListView){
            val prefixName = "${CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, upperCase(moduleName))}_"
            val path = psiPath + folder
            generateViewFile(path, prefixName)
        }
        if(data.Controller){
            val prefixName = "${CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, upperCase(moduleName))}_"
            val path = psiPath + folder
            generateController(path, prefixName)
        }
    }
    //region private function for List View
    private fun generateViewFile(path: String, prefixName: String) {
        generateFile(
            "ListView.dart", path, "${prefixName}${data.list.ListViewName.lowercase(Locale.getDefault())}.dart"
        )
    }
    //endregion

    //region private function for Controller
    private fun generateController(path: String, prefixName: String) {
        generateFile(
            "Controller.dart", path,"${prefixName}${data.controller.ControllerName.lowercase(Locale.getDefault())}.dart"
        )
    }
    //endregion

    //region private function for Api Module
    private fun generateApiModule(path: String) {
        val apiFiles = listOf(
            "api_fetch_data.dart",
            "api_executor.dart",
            "api_name_const.dart",
            "api_parameters.dart",
            "api_service.dart",
            "information.txt"
        )

        apiFiles.forEach { fileName ->
            generateFile(fileName, path, fileName)
        }
    }
    //endregion

    //region Common file to generate file
    private fun generateFile(inputFileName: String, filePath: String, outFileName: String) {
        val content = dealContent(inputFileName)
        try {
            val folder = File(filePath)
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
    //endregion


    private fun dealContent(inputFileName: String): String {
        val name = toCamelCase(moduleName)
//        val prefixName = "${CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)}_"
        var content = getSuitableContentFile(inputFileName)
        replaceContentMap.clear()

        replaceContentMap["@name"] = name
        replaceContentMap.forEach { (key, value) ->
            content = content.replace(key.toRegex(), value)
        }
        return content
    }

    private fun getSuitableContentFile(inputFileName: String): String {
        var defaultFolder = ""

        //region For List View
        if(data.ListView) {
            defaultFolder = "/templates/ListView"
        }
        //endregion

        //region For Controller
        if (data.Controller) {
            defaultFolder = "/templates/Controller"
        }
        //endregion

        //region For Api Module
        if(data.ApiModule) {
            defaultFolder = "/templates/Api"
        }
        //endregion

        return try {
            val resourcePath = "$defaultFolder/$inputFileName"
            val input = this.javaClass.getResourceAsStream(resourcePath)
                ?: throw FileNotFoundException("Template file not found: $resourcePath")
            String(readStream(input))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    @Throws(Exception::class)
    private fun readStream(inStream: InputStream): ByteArray {
        val outStream = ByteArrayOutputStream()
        try {
            val buffer = ByteArray(1024)
            var len: Int
            while (inStream.read(buffer).also { len = it } != -1) {
                outStream.write(buffer, 0, len)
            }
        } finally {
            outStream.close()
            inStream.close()
        }
        return outStream.toByteArray()
    }

    private fun upperCase(str: String): String {
        return str.substring(0, 1).uppercase(Locale.getDefault()) + str.substring(1)
    }
    private fun toCamelCase(input: String): String {
        return input.split("_").joinToString("") { it.replaceFirstChar { char -> char.uppercaseChar() } }
    }

    private var replaceContentMap = HashMap<String, String>()
}
