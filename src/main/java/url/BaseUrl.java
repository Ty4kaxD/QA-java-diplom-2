package url;

import io.restassured.RestAssured;

public class BaseUrl {
    public static void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }
}
