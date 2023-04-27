
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class PathAndJsonPath {

    @Test
    public void extractingPath(){
        // "post code": "90210",

        String postCode=     // mesela burada int yapsak kendi donusturmez
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().path("'post code'");
        ;

        System.out.println("postCode = " + postCode);
    }

    @Test
    public void extractingJsonPath(){
        // "post code": "90210",
        int postCode=
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("'post code'")   // kendi donusturup veriyor
                // tip dönüşümü otomatik, uygun tip verilmeli
                ;

        System.out.println("postCode = " + postCode);
    }
}
