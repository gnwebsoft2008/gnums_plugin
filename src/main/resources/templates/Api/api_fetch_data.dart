import 'dart:convert';
import 'package:get/get.dart';

class ApiFetchData {

  //region 1.0.0 Get Data From Api...
  static Future<List<T>> fetchApiData<T>({
    required int apiCode,
    required bool isNew,
    required bool isClose,
    required bool isShowDialog,
    required Map<String, String> parameters,
    required List<T> Function(Map<String, dynamic>) extractItems,
  }) async {
    List<T> items = [];
    items.clear();
    ApiExecutor ae = ApiExecutor();
    await ae.callApi(
      context: Get.context,
      apiCode: apiCode,
      isNew: isNew,
      isClose: isClose,
      isShowDialog: isShowDialog,
      data: parameters,
      onGetData: (val) {
        final data = jsonDecode(val);
        items.addAll(extractItems(data));
      },
    );
    return items;
  }
//endregion

}
