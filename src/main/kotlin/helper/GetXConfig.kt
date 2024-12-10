package helper

import com.google.gson.Gson
import com.intellij.util.xmlb.Converter

data class TemplateInfo(
    var logic: String = "Logic",
    var view: String = "Page",
    var viewFile: String = "View",
//    var state: String = "State",
    var selected: Boolean = false,
)

///select function
data class FunctionInfo(
    //default true
    var useGetX5: Boolean = true,
    //default true
//    var useFolder: Boolean = false,
    //default false
    var usePrefix: Boolean = true,
    //default false
    var isPageView: Boolean = true,
    //auto dispose GetXController
//    var autoDispose: Boolean = false,
    //add Lifecycle
//    var addLifecycle: Boolean = false,
    //add binding
//    var addBinding: Boolean = false,
    //function tab index
    var funTabIndex: Int = 0,
)

//module name
data class ModuleNameSuffix(
    //Logical layer name
    var logicName: String = "Logic",
    //view layer name
//    var viewName: String = "Page",
    var viewFileName: String = "View",
    //state layer name
//    var stateName: String = "State",
)


//region List View region
data class ListView(
    var ListViewName: String = "ListView",
)
class ListViewConverter : Converter<ListView>(){
    override fun toString(value: ListView): String? {
        return Gson().toJson(value)
    }

    override fun fromString(value: String): ListView? {
        return Gson().fromJson(value, ListView::class.java)
    }

}
//endregion


//Setting Info
data class SettingInfo(
    //open folder suffix
    var useFolderSuffix: Boolean = false,
)

class ModuleNameSuffixConverter : Converter<ModuleNameSuffix>() {
    override fun toString(value: ModuleNameSuffix): String? {
        return Gson().toJson(value)
    }

    override fun fromString(value: String): ModuleNameSuffix? {
        return Gson().fromJson(value, ModuleNameSuffix::class.java)
    }
}

class FunctionInfoConverter : Converter<FunctionInfo>() {
    override fun toString(value: FunctionInfo): String? {
        return Gson().toJson(value)
    }

    override fun fromString(value: String): FunctionInfo? {
        return Gson().fromJson(value, FunctionInfo::class.java)
    }
}

class SettingInfoConverter : Converter<SettingInfo>() {
    override fun toString(value: SettingInfo): String? {
        return Gson().toJson(value)
    }

    override fun fromString(value: String): SettingInfo? {
        return Gson().fromJson(value, SettingInfo::class.java)
    }
}

class TemplateInfoConverter : Converter<TemplateInfo>() {
    override fun toString(value: TemplateInfo): String? {
        return Gson().toJson(value)
    }

    override fun fromString(value: String): TemplateInfo? {
        return Gson().fromJson(value, TemplateInfo::class.java)
    }
}
