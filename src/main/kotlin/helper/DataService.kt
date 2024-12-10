package helper

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.OptionTag

//val modeInfoConverter: KClass<out Converter<ModeInfoConverter>> = ModeInfoConverter::class;

//custom save location
@State(name = "DataService", storages = [Storage(value = "DataService.xml")])
class DataService : PersistentStateComponent<DataService> {
//    var modeDefault = true

    //default true: use default mode
//    @JvmField
    //default true：default not use easy mode
    @JvmField
    var modeEasy = true

    @JvmField
    var Controller = false

    @JvmField
    var ListView = false

    //module name suffix
    @JvmField
    @OptionTag(converter = ModuleNameSuffixConverter::class)
    var module = ModuleNameSuffix(
//        viewName = "Page",
        viewFileName = "View",
        logicName = "Logic",
//        stateName = "State",
    )

    //region list view...
    @OptionTag(converter = ListViewConverter::class)
    var list = ListView(
        ListViewName = "ListView"
    )
    //endregion

    //select function
    @JvmField
    @OptionTag(converter = FunctionInfoConverter::class)
    var function = FunctionInfo(
//        useGetX5 = true,
//        useFolder = false,
        usePrefix = true,
        isPageView = true,
//        addBinding = false,
//        addLifecycle = false,
//        autoDispose = false,
        funTabIndex = 0,
    )

    //setting info
    @JvmField
    @OptionTag(converter = SettingInfoConverter::class)
    var setting = SettingInfo(useFolderSuffix = false)

    ///default true
//    @JvmField
//    @OptionTag(converter = TemplateInfoConverter::class)
//    var templatePage = TemplateInfo(view = "Page", selected = true)

    ///default false
//    @JvmField
//    @OptionTag(converter = TemplateInfoConverter::class)
//    var templateComponent = TemplateInfo(view = "Component", selected = false)

    ///default false
    @JvmField
    @OptionTag(converter = TemplateInfoConverter::class)
    var templateCustom = TemplateInfo(
        view = "Widget",
        selected = true)


    override fun getState(): DataService {
        return this
    }

    override fun loadState(state: DataService) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        @JvmStatic
        val instance: DataService
            get() = ApplicationManager.getApplication().getService(DataService::class.java)
    }
}