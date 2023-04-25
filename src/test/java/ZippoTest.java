import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;  // bunu hepsine manuel ekliyoruz
import static org.hamcrest.Matchers.*;       // bunu da

public class ZippoTest {

    @Test
    public void test() {

        given()
                // hazirlik islemleri : (token, send body, parametreler)

                .when()
                // endpoint(url), metodu

                .then()
        // assertion, test, data islemleri

        ;

    }

    @Test
    public void statusCodeTest() {

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
    public void contentTypeTest() {

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
    public void checkCountryInResponseBody() {

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

    @Test
    public void checkStateInResponseBody() {

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
               //  .log().body()

                .statusCode(200)
                .body("places[0].state", equalTo("California"));

    }

    @Test
    public void checkHasItemy() {  // equalTo ile ayni calisir

        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                // .log().body()

                .statusCode(200)
                .body("places.'place name'", hasItem("Dörtağaç Köyü"));
        // butun place name lerin herhangi birinde Dortagac koyu var mi?

    }

    @Test
    public void bodyArrayHasSizeTest() {  // equalTo ile ayni calisir

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                //.log().body()

                .statusCode(200)
                .body("places", hasSize(1));


    }


    @Test
    public void combiningTest() {  // equalTo ile ayni calisir

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                //.log().body()

                .statusCode(200)
                .body("places.state", hasItem("California")) // verilen path deki list bu iteme sahip mi
                .body("places[0].'place name'", equalTo("Beverly Hills")); // verilen path de ki deger buna esit mi


    }


//    http://api.zippopotam.us/us/90210    path PARAM
//
//    https://sonuc.osym.gov.tr/Sorgu.aspx?SonucID=9617  Query PARAM
    @Test
    public void pathParamTest(){
        given()
                .pathParam("ulke","us")
                .pathParam("postaKod", 90210)
                .log().uri()  // request link

                .when()
                .get("https://api.zippopotam.us/{ulke}/{postaKod}")

                .then()
                .statusCode(200)
                //.log().body()

                ;
    }


    @Test
    public void queryParamTest(){

        // https://gorest.co.in/public/v1/users?page=2

        given()
                .param("page", 1)
                .log().uri()  // request link

                .when()
                .get("https://gorest.co.in/public/v1/users")  // ?`page=1

                .then()
                .statusCode(200)
                .log().body()

        ;
    }


    @Test
    public void queryParamTest2(){

        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.

        for (int i = 1; i < 10 ; i++) {
            given()
                    .param("page", i)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .statusCode(200)
                    .log().body()
                    .body("meta.pagination.page", equalTo(i))

            ;
        }

    }

    RequestSpecification requestSpec;  // istek
    ResponseSpecification responseSpec;  // donus

    @BeforeClass     // kullanmadan once calismasi gerektigi icin beforeclass yaptik
    public void Setup(){

        baseURI = "https://gorest.co.in/public/v1";  // bu kendi calisiyor cagirmana gerek yok

        requestSpec = new RequestSpecBuilder()   // bu kodlari tekrar yazmamak icin buraya koyduk
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)   // bu code baska gelecekse burayi guncellemek gerekir
                .log(LogDetail.BODY)
                .build();
    }



    @Test
    public void requestResponseSpecification()
    {

        given()
                .param("page",1)
                .spec(requestSpec)

                .when()
                .get("/users") // base URI oldugu icin sadece bunu yazdik. onden sabitledik

                .then()
                .spec(responseSpec)
        ;
    }



}


//    PM                            RestAssured
//    body.country                  body("country")
//    body.'post code'              body("post code")
//    body.places[0].'place name'   body("places[0].'place name'")
//    body.places.'place name'      body("places.'place name'")
//    bütün place nameleri bir arraylist olarak verir
//
//    {
//        "post code": "90210",
//            "country": "United States",
//            "country abbreviation": "US",
//            "places": [
//        {
//            "place name": "Beverly Hills",
//                "longitude": "-118.4065",
//                "state": "California",
//                "state abbreviation": "CA",
//                "latitude": "34.0901"
//        }
//    ]


