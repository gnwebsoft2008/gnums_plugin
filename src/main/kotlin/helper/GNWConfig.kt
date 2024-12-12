package helper

import com.google.gson.Gson
import com.intellij.util.xmlb.Converter


//region 1.0.0 GNW List View...
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

//region 2.0.0 GNW Controller...
data class Controller(
    var ControllerName: String = "Controller",
)
class ControllerConverter : Converter<Controller>(){
    override fun toString(value: Controller): String? {
        return Gson().toJson(value)
    }
    override fun fromString(value: String): Controller? {
        return Gson().fromJson(value, Controller::class.java)
    }
}
//endregion

//region 3.0.0 GNW Api Fetch
data class ApiFetch(
    var FetchApiName:String = "FetchApi",
)
class ApiFetchConverter : Converter<ApiFetch>(){
    override fun toString(value: ApiFetch): String? {
        return Gson().toJson(value)
    }
    override fun fromString(value: String): ApiFetch? {
        return Gson().fromJson(value, ApiFetch::class.java)
    }
}
//endregion
