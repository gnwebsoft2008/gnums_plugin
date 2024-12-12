package helper

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.OptionTag

//custom save location
@State(name = "DataService", storages = [Storage(value = "DataService.xml")])
class DataService : PersistentStateComponent<DataService> {

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
    var FetchApi = false
    @OptionTag(converter = ApiFetchConverter::class)
    var fetchApi = ApiFetch(
        FetchApiName = "FetchApi"
    )
    //endregion

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