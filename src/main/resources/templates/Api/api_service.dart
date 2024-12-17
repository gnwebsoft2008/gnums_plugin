import 'package:dio/dio.dart';
import 'package:pretty_dio_logger/pretty_dio_logger.dart';
import 'package:retrofit/retrofit.dart';

/// Run the following command to generate the file [api_service.g.dart]: [flutter pub run build_runner build]
part 'api_service.g.dart';

@RestApi(baseUrl: '') // Set the base URL for your API here

//region 1.0.0 Api Service Class
abstract class ApiService {
  /// Factory constructor to initialize the [ApiService].
  factory ApiService(Dio dio, {String baseUrl}) = _ApiService;

  ///region 1.1.0 Creates an instance of [ApiService] with default configurations.
  static ApiService create() {
    final dio = Dio();

    ///This includes a [PrettyDioLogger] interceptor for debugging.
    dio.interceptors.add(
      PrettyDioLogger(
        request: true,
        requestBody: true,
        requestHeader: true,
        responseBody: true,
        responseHeader: true,
      ),
    );

    return ApiService(dio);
  }

  ///endregion

  //region 1.2.0 API Methods

  //region 1.2.1 Makes a POST request to the specified endpoint.
  @POST("post-endpoint") // Update with the actual endpoint
  Future<String> postRequest(); // change the name as per your requirement and also give necessary parameter
  //endregion

  //region 1.2.2 Makes a GET request to the specified endpoint.
  @GET("get-endpoint") // Update with the actual endpoint
  Future<String> getRequest(); // change the name as per your requirement and also give necessary parameter
  //endregion

  //region 1.2.3 Makes a DELETE request to the specified endpoint.
  @DELETE("delete-endpoint") // Update with the actual endpoint
  Future<String> deleteRequest(); // change the name as per your requirement and also give necessary parameter
  //endregion

  //region 1.2.4 Makes a PUT request to the specified endpoint.
  @DELETE("put-endpoint") // Update with the actual endpoint
  Future<String> putRequest(); // change the name as per your requirement and also give necessary parameter
//endregion

//endregion
}
//endregion
