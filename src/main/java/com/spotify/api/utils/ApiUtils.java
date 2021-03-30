package com.spotify.api.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;


public class ApiUtils {
    protected static RequestSpecification requestSpec;
    protected static String access_token;
    protected static ValidatableResponse response;

    public static void setAccessToken(String auth){
        Map<String,String> formParams = new HashMap<>();
        formParams.put("grant_type", ConfigurationReader.getProperty("grant_type"));
        formParams.put("refresh_token",ConfigurationReader.getProperty("refresh_token"));

        access_token =
                given()
                        .contentType(ContentType.URLENC)
                        .accept(ContentType.JSON)
                        .formParams(formParams)
                        .header("Authorization",auth).
                when()
                        .post("https://accounts.spotify.com/api/token").
                then()
                        .statusCode(200)
                        .extract().response().path("access_token");
    }

    public static String getAccessToken(){
        return access_token;
    }

    public static RequestSpecification setBaseReqSpec(){
        requestSpec = given()
                .accept(ContentType.JSON)
                .log().ifValidationFails()
                .auth().oauth2(access_token);

        return requestSpec;
    }

    public static RequestSpecification setCustomReqSpec( Map<String,Object> params){
        requestSpec
                .pathParams(params);
        return requestSpec;
    }

    public static void resetRequestSpec(){
        reset();
        baseURI = ConfigurationReader.getProperty("base_url");
        setBaseReqSpec();
    }

    public static RequestSpecification setQueryParam(Map<String,Object> params){
        requestSpec
                .queryParams(params);
        return requestSpec;
    }

    public static ValidatableResponse get(String url){
        return get(url,200);
    }

    public static ValidatableResponse get(String url, int statusCode){
        response = given().spec(requestSpec)
                .when().get(url)
                .then().statusCode(is(statusCode));
        return response;
    }

    public static ValidatableResponse post(String url){
        return post(url,"",200);
    }

    public static ValidatableResponse post(String url, int statusCode){
        return post(url,"",statusCode);
    }

    public static ValidatableResponse post(String url, Object body){
        return post(url,body,200);
    }

    public static ValidatableResponse post(String url, Object body, int statusCode){
        response = given().spec(requestSpec)
                .body(body)
                .when().post(url)
                .then().log().ifError()
                .statusCode(is(statusCode));
        return response;
    }

    public static ValidatableResponse postFile(String url, File file){
        response = given().spec(requestSpec)
                .body(file)
                .when().post(url)
                .then();
        return response;
    }

    public static ValidatableResponse put(String url){
        return put(url,"",200);
    }

    public static ValidatableResponse put(String url, int statusCode){
        return put(url,"",statusCode);
    }

    public static ValidatableResponse put(String url, Object body){
        return put(url,body,200);
    }

    public static ValidatableResponse put(String url, Object body, int statusCode){
        response = given().spec(requestSpec)
                .body(body)
                .when().put(url)
                .then().log().ifError()
                .statusCode(is(statusCode));
        return response;
    }

    public static ValidatableResponse patch(String url){
        return patch(url,"",200);
    }

    public static ValidatableResponse patch(String url, int statusCode){
        return patch(url,"",statusCode);
    }

    public static ValidatableResponse patch(String url, Object body){
        return patch(url,body,200);
    }

    public static ValidatableResponse patch(String url, Object body, int statusCode){
        response = given().spec(requestSpec)
                .body(body)
                .when().patch(url)
                .then().log().ifError()
                .statusCode(is(statusCode));
        return response;
    }

    public static ValidatableResponse delete(String url){
        return delete(url,"",200);
    }

    public static ValidatableResponse delete(String url, int statusCode){
        return delete(url,"",statusCode);
    }

    public static ValidatableResponse delete(String url, Object body){
        return delete(url,body,200);
    }

    public static ValidatableResponse delete(String url, Object body, int statusCode){
        response = given().spec(requestSpec)
                .body(body)
                .when().delete(url)
                .then().log().ifError()
                .statusCode(is(statusCode));
        return response;
    }

    public static ArrayNode extractAsArrayNode(String path){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(response.extract().response().path(path));
    }

    public static JsonNode extractAsJsonNode(String path){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(response.extract().response().path(path));
    }

    public static JsonNode StringToJson(String content){
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jn = null;
        try {
            jn = mapper.readValue(content,JsonNode.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jn;
    }

    public static JsonNode extractAsJsonNode(){
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jn = null;
        try {
            jn = mapper.readValue(response.extract().asString(),JsonNode.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jn;
    }

    public static List<Object> extractList(String jsonPath,Class clazz){
        try {
            return response.extract().jsonPath().getList(jsonPath,clazz);
        }catch (NullPointerException e){
            return null;
        }
    }

    public static ValidatableResponse getResponse(){
        return response;
    }

    public static List<String> shuffleStreamNode(){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        return StreamSupport.stream(node.spliterator(),false).map(each->each.get("field").asText())
                            .collect(Collectors.collectingAndThen(Collectors.toList(),collected-> {
                                Collections.shuffle(collected);
                                return collected.stream();
                            })).collect(Collectors.toList());
    }

    public static void apiWait(int mili){
        try {
            Thread.sleep(mili);
        }catch (Exception e){
        }
    }
}
