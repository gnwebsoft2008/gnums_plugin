package GNW_Helper

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.OptionTag

//custom save location
//@State(name = "DataService", storages = [Storage(value = "DataService.xml")])
class GNWDataService : PersistentStateComponent<GNWDataService> {

    //region GNW List View...
    @JvmField
    var ListView = true
    @OptionTag(converter = ListViewConverter::class)
    var list = ListView(
        ListViewName = "ListView"
    )
    //endregion

    //region GNW Controller...
    @JvmField
    var Controller = false
    @OptionTag(converter = ControllerConverter::class)
    var controller = Controller(
        ControllerName = "Controller"
    )
    //endregion

    //region GNW Api Fetch...
    @JvmField
    var ApiModule = false
    @OptionTag(converter = ApiModuleConverter::class)
    var apiModule = ApiModule(
        ApiModuleName = "ApiModule"
    )
    //endregion

    override fun getState(): GNWDataService {
        return this
    }

    override fun loadState(state: GNWDataService) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        @JvmStatic
        val instance: GNWDataService
            get() = ApplicationManager.getApplication().getService(GNWDataService::class.java)
    }
}