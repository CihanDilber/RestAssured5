import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;  // bunu hepsine manuel ekliyoruz
import static org.hamcrest.Matchers.*;       // bunu da

public class ZippoTest {

    @Test
    public void test(){

        given()
                // hazirlik islemleri : (token, send body, parametreler)

                .when()
                // endpoint(url), metodu

                .then()
                // assertion, test, data islemleri

        ;

    }

    @Test
    public void statusCodeTest(){

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .log().body()          // donen body jason datasi. hepsini gostersin istersen    log.all()  yazilir
                                       // bu kisim her zaman calismasina gerek yok. gormek icin
                .statusCode(200)       // donus kodu 200 mu

                ;

    }

    @Test
    public void contentTypeTest(){

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .log().body()          // donen body jason datasi. hepsini gostersin istersen    log.all()  yazilir
                // bu kisim her zaman calismasina gerek yok. gormek icin
                .statusCode(200)       // donus kodu 200 mu
                .contentType(ContentType.JSON)  // donen sonuc JSON mi

        ;

    }


    @Test
    public void checkCountryInResponseBody(){

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .log().body()          // donen body jason datasi. hepsini gostersin istersen    log.all()  yazilir
                // bu kisim her zaman calismasina gerek yok. gormek icin
                .statusCode(200)       // donus kodu 200 mu
                .body("country", equalTo("United States"))  // body nin country degiskeni "United States" esit MI

        // postman de soyle yapmistik
        // pm.response.json.id --> body.id



        ;

    }
}
