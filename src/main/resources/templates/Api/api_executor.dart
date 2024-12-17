import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:get/get.dart';
import 'package:progress_dialog_null_safe/progress_dialog_null_safe.dart';
import 'package:provider/provider.dart';
import 'api_service.dart';
import 'package:dio/dio.dart';

//region 1.0.0 Api Executor class to handle API calls with a progress dialog and error handling.
class ApiExecutor with DialogMixin {

  //region 1.1.0 Variable Declaration
  /// Progress dialog instance for showing loading status.
  late ProgressDialog pd;
  //endregion

  //region 1.2.0 Function to open dialog box
  /// Displays a progress dialog to indicate ongoing operations.
  /// [context] is the BuildContext of the current widget.
  /// [isNew] indicates whether to show a new dialog.
  Future<void> showPd(context, isNew) async {
    pd = ProgressDialog(
      context,
      type: ProgressDialogType.normal,
      isDismissible: true,
    );
    pd.style(
      padding: const EdgeInsets.all(20),
      elevation: 10,
      message: 'Please wait...',
      progressWidget: const SpinKitCircle(
        color: Colors.black,
        size: 30.0,
      ),
      insetAnimCurve: Curves.easeInOut,
    );
    if (isNew) {
      await pd.show();
    }
  }

  //endregion

  //region 1.3.0 Function to close the dialog box

  /// Hides the progress dialog if it is open.
  /// [isClose] indicates whether to hide the dialog.
  Future<void> dismissPd(isClose) async {
    if (isClose) {
      await pd.hide();
    }
  }

  //endregion

  //region 1.4.0 Executes an API call and handles success or error responses.
  /// [context] is the BuildContext of the current widget.
  /// [apiCode] specifies the API code to identify the endpoint.
  /// [isNew] indicates whether to show a new progress dialog.
  /// [isClose] specifies whether to close the dialog after the API call.
  /// [data] contains the request payload (optional).
  /// [onGetData] is a callback function to handle the response.
  /// [isShowDialog] determines whether to display dialogs (optional).
  Future<void> callApi({
    required BuildContext context,
    required int apiCode,
    required bool isNew,
    required bool isClose,
    Map<String, dynamic>? data,
    Function? onGetData,
    bool? isShowDialog,
  }) async {
    /// Checks internet connection availability.
    bool isInternetAvailable = await ConnectionStatusSingleton.getInstance().checkConnection();

    if (isInternetAvailable) {

      /// Display the progress dialog to inform the user that an operation is in progress.
      await showPd(context, isNew);

      /// Retrieve an instance of ApiService from the Provider for making API calls.
      /// The 'listen: false' parameter ensures that this widget does not rebuild if the ApiService changes.
      final api = Provider.of<ApiService>(context, listen: false);

      /// Declare a Future to store the API response. This will be used to execute the selected API call later.
      Future<dynamic>? future;

      //region 1.4.1 Determine which API to call based on the [apiCode].
      switch (apiCode) {
        case demoApiCode:
          future = api.getRequest();
          break;
      }
      //endregion

      //region 1.4.2 Handle API response or error.
      await future!.then(
        (value) async {
          await dismissPd(isClose);
          onGetData!(value);
        },
        onError: (data) async {
          await dismissPd(true);
          DioException error = data;

          if (error.response!.statusMessage == 'Unauthorized') {
            // Handle unauthorized error specifically (e.g., redirect to login).
          } else {
            /// Show an alert dialog for other errors.
            showAlertDialog(
              Get.context,
              'Alert',
              error.response!.statusMessage ?? 'Something went wrong',
              'Okay',
              null,
              (val) {},
            );
          }
        },
      );

      //endregion

    } else {

      //region 1.4.3 If no internet connection, dismiss the dialog and show an alert.
      await dismissPd(true);
      Future.delayed(
        Duration.zero,
        () async {
          showAlertDialog(
            context,
            'Internet',
            'Please check your internet connection',
            'Ok',
            null,
            (val) {},
          );
        },
      );
      //endregion
    }
  }
//endregion

}
//endregion
