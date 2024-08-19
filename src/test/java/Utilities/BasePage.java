package Utilities;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.output.WriterOutputStream;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static io.restassured.config.DecoderConfig.decoderConfig;

public abstract class BasePage {

    protected StringWriter request, response;
    protected enum requestTypes {GET, POST, PUT, DELETE;}

    protected Filter setRequestLog()
    {
        request = new StringWriter();
        return RequestLoggingFilter.logRequestTo(new PrintStream(new WriterOutputStream(request, StandardCharsets.UTF_8), true));
    }

    protected Filter setResponseLog()
    {
        response = new StringWriter();
        return ResponseLoggingFilter.logResponseTo(new PrintStream(new WriterOutputStream(response, StandardCharsets.UTF_8), true));
    }

    /**
     *Method to sent API request
     *
     * @param baseURL   The host of API request
     * @param port      The API port e.g. 8080 and null if not needed
     * @param endpoint  The API endpoint e.g. /API/GetUsers
     * @param requestTypes The API request Type e.g. "POST, GET, PUT, DELETE"
     * @param headers   The request headers (TOKENS or request headers)
     * @param requestBody The request body, and it's optional and send null if not needed
     * @return Response object for the API has been sent
     */
    protected Response sendAPIRequest(final String baseURL, final Integer port, final String endpoint, final requestTypes requestTypes, final Headers headers, final Map<Object, Object> requestBody)
    {
        RestAssured.config = RestAssured.config().decoderConfig(decoderConfig().noContentDecoders());
        RequestSpecification requestSpecification = RestAssured.given()
                .baseUri(baseURL);

        if (port != null) requestSpecification.port(port);
        requestSpecification.contentType(ContentType.JSON)
                .filter(setRequestLog())
                .filter(setResponseLog())
                .headers(headers);

        if (requestBody != null) requestSpecification.body(requestBody);
        Response apiResponse;
        apiResponse = switch (requestTypes)
        {
            case GET -> requestSpecification.get(endpoint);
            case POST -> requestSpecification.post(endpoint);
            case PUT -> requestSpecification.put(endpoint);
            case DELETE -> requestSpecification.delete(endpoint);
        };

        return apiResponse;
    }

    /**
     *Method to sent API request with string payload after construct it to json payload
     *
     * @param baseURL   The host of API request
     * @param port      The API port e.g. 8080 and null if not needed
     * @param endpoint  The API endpoint e.g. /API/GetUsers
     * @param requestTypes The API request Type e.g. "POST, GET, PUT, DELETE"
     * @param headers   The request headers (TOKENS or request headers)
     * @param requestBodyPayload The request String payload, and it's optional and send null if not needed
     * @return Response object for the API has been sent
     */
    protected Response sendAPIRequestWithStringPayload(final String baseURL, final Integer port, final String endpoint, final requestTypes requestTypes, final Headers headers, final String requestBodyPayload) {
        RestAssured.config = RestAssured.config().decoderConfig(decoderConfig().noContentDecoders());
        RequestSpecification requestSpecification = RestAssured.given()
                .baseUri(baseURL);
        if (port != null) requestSpecification.port(port);
        requestSpecification.contentType(ContentType.JSON)
                .filter(setRequestLog())
                .filter(setResponseLog())
                .headers(headers);

        if (requestBodyPayload != null) requestSpecification.body(requestBodyPayload);
        Response apiResponse;
        apiResponse = switch (requestTypes)
        {
            case GET -> requestSpecification.get(endpoint);
            case POST -> requestSpecification.post(endpoint);
            case PUT -> requestSpecification.put(endpoint);
            case DELETE -> requestSpecification.delete(endpoint);
        };

        return apiResponse;
    }

    /**
     *Method to sent API request with query params
     *
     * @param baseURL   The host of API request
     * @param port      The API port e.g. 8080 and null if not needed
     * @param endpoint  The API endpoint e.g. /API/GetUsers
     * @param queryParams  The Query params it's Map.of("key1","Value1","Key2","Value2")
     * @param requestTypes The API request Type e.g. "POST, GET, PUT, DELETE"
     * @param headers   The request headers (TOKENS or request headers)
     * @param requestBodyPayload The request String payload, and it's optional and send null if not needed
     * @return Response object for the API has been sent
     */
    protected Response sendAPIRequestWithQueryParams(final String baseURL, final Integer port, final String endpoint, final Map<String, ?> queryParams, final requestTypes requestTypes, final Headers headers, final String requestBodyPayload) {
        RestAssured.config = RestAssured.config().decoderConfig(decoderConfig().noContentDecoders());
        RequestSpecification requestSpecification = RestAssured.given()
                .baseUri(baseURL)
                .queryParams(queryParams);
        if (port != null) requestSpecification.port(port);
        requestSpecification.contentType(ContentType.JSON)
                .filter(setRequestLog())
                .filter(setResponseLog())
                .headers(headers);

        if (requestBodyPayload != null) requestSpecification.body(requestBodyPayload);
        Response apiResponse;
        apiResponse = switch (requestTypes)
        {
            case GET -> requestSpecification.get(endpoint);
            case POST -> requestSpecification.post(endpoint);
            case PUT -> requestSpecification.put(endpoint);
            case DELETE -> requestSpecification.delete(endpoint);
        };

        return apiResponse;
    }


    /**
     *Method to sent API request with Image
     *
     * @param baseURL   The host of API request
     * @param port      The API port e.g. 8080 and null if not needed
     * @param endpoint  The API endpoint e.g. /API/GetUsers
     * @param contentType The API content type e.g. ContentType.MULTIPART for sending images
     * @param requestTypes The API request Type e.g. "POST, GET, PUT, DELETE"
     * @param headers   The request headers (TOKENS or request headers)
     * @param filePath  The Image file path
     * @return Response object for the API has been sent
     */
    protected Response sendAPIRequestWithImage(final String baseURL, final Integer port, final String endpoint, final ContentType contentType, final requestTypes requestTypes, final Headers headers, final String filePath) {
        RestAssured.config = RestAssured.config().decoderConfig(decoderConfig().noContentDecoders());
        RequestSpecification requestSpecification = RestAssured.given()
                .baseUri(baseURL)
                .contentType(contentType);
        if (port != null) requestSpecification.port(port);
        requestSpecification.filter(setRequestLog())
                .filter(setResponseLog())
                .headers(headers)
                .when()
                .multiPart("file", new File(filePath));
        Response apiResponse;
        apiResponse = switch (requestTypes)
        {
            case GET -> requestSpecification.get(endpoint);
            case POST -> requestSpecification.post(endpoint);
            case PUT -> requestSpecification.put(endpoint);
            case DELETE -> requestSpecification.delete(endpoint);
        };

        return apiResponse;
    }


    /**
     *Method to sent API request with Customized content type
     *
     * @param baseURL   The host of API request
     * @param port      The API port e.g. 8080 and null if not needed
     * @param endpoint  The API endpoint e.g. /API/GetUsers
     * @param contentType The API content type e.g. ContentType.JSON or ContentType. BINARY for images
     * @param requestTypes The API request Type e.g. "POST, GET, PUT, DELETE"
     * @param headers   The request headers (TOKENS or request headers)
     * @param requestBody  The Request body and set it with null if not needed
     * @return Response object for the API has been sent
     */
    protected Response sendAPIRequestWithContentType(final String baseURL, final Integer port, final String endpoint, final ContentType contentType, final requestTypes requestTypes, final Headers headers, final String requestBody) {
        RestAssured.config = RestAssured.config().decoderConfig(decoderConfig().noContentDecoders());
        RequestSpecification requestSpecification = RestAssured.given()
                .baseUri(baseURL);
        if (port != null) requestSpecification.port(port);
        requestSpecification.filter(setRequestLog())
                .filter(setRequestLog())
                .filter(setResponseLog())
                .headers(headers);
        if (requestBody != null) requestSpecification.body(requestBody);
        Response apiResponse;
        apiResponse = switch (requestTypes)
        {
            case GET -> requestSpecification.get(endpoint);
            case POST -> requestSpecification.post(endpoint);
            case PUT -> requestSpecification.put(endpoint);
            case DELETE -> requestSpecification.delete(endpoint);
        };

        return apiResponse;
    }


    //region helper functions for Base Test methods
    /**
     * Method to construct Json Payload from string file
     */
    public static String constructJsonPayload(String filePath)
    {
        try {
            byte[] encodedBytes = Files.readAllBytes(Paths.get(filePath));
            String jsonString = new String(encodedBytes,StandardCharsets.UTF_8);
            if (isStringValidJson(jsonString)){
                return jsonString;
            }

        }catch (IOException ex)
        {
            ex.printStackTrace();
        }
        throw new Error("Failed to read or invalid JSON");
    }

    /**
     * Method to validate if the string valid to json
     *
     */
    public static boolean isStringValidJson(String str)
    {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(str);
            return true;
        }catch (IOException ex){
            return false;
        }
    }
//endregion

}
