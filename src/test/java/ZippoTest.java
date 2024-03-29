import Model.Location;
import Model.Place;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

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
                .log().body()          // donen body jason datasi. hepsini gostersin istersen    log.all()  yazilir, data disindaki bilgileri verir
                // bu kisim her zaman calismasina gerek yok. gormek icin
                .statusCode(200)       // donus kodu 200 MU?  then den sonra yazdiklarin assertion, test, data islemleri

        ;

    }

    @Test
    public void contentTypeTest() {

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)       // donus kodu 200 mu
                .contentType(ContentType.JSON)  // donen sonuc JSON mi

        ;

    }


    @Test
    public void checkCountryInResponseBody() {  // bilgiyi kontrol ettik burada

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)       // donus kodu 200 mu
                .body("country", equalTo("United States"))  // body nin country degiskeni "United States" esit MI
                                                            // datayi disina almadan icerde kontrol ediyorsun. hamcrest sayesinde. yukarda ekliyoruz

        // postman de soyle yapmistik
        // pm.response.json.id --> body.id


        ;
    }
    // https://jsonpathfinder.com/
    //    Postman                     RestAssured

//    body.country                  body("country")
//    body.'post code'              body("post code")                    // post code arada bosluk icerdigi icin '' icine alindi
//    body.places[0].'place name'   body("places[0].'place name'")
//    body.places.'place name'      body("places.'place name'")         //    bütün place nameleri bir arraylist olarak verir

//
//    { --> body
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


    @Test
    public void checkStateInResponseBody() {

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                //  .log().body()             // burasi zorunlu degil. gormek istersek aciyoruz

                .statusCode(200)
                .body("places[0].state", equalTo("California"))
        ;

    }

    @Test
    public void checkHasItemTest() {

        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                //.log().body()

                .statusCode(200)
                .body("places.'place name'", hasItem("Dörtağaç Köyü"));    // butun place name lerin herhangi birinde Dortagac koyu var mi?
                                                                           // bu dizi bunu iceriyor mu?


    }

    @Test
    public void bodyArrayHasSizeTest() {

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                //.log().body()                  // once burayi aciyoruz bakiyoruz size ne kadar sonra kapattik

                .statusCode(200)
                .body("places", hasSize(1));


    }


    @Test
    public void combiningTest() {

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                //.log().body()

                .statusCode(200)              // yani birden fazla testi alt alta yazabiliyoruz
                .body("places", hasSize(1))  // size ı 1 mi
                .body("places.state", hasItem("California")) // verilen path deki list bu iteme sahip mi
                .body("places[0].'place name'", equalTo("Beverly Hills")); // verilen path de ki deger buna esit mi


    }


    //    http://api.zippopotam.us/us/90210    path PARAM    // bu 1. yontem
//
//    https://sonuc.osym.gov.tr/Sorgu.aspx?SonucID=9617  Query PARAM   // bu da 2. yontem
    @Test
    public void pathParamTest() {
        given()
                .pathParam("ulke", "us")
                .pathParam("postaKod", 90210)
                .log().uri()  // request URL

                .when()
                .get("https://api.zippopotam.us/{ulke}/{postaKod}")    // given in altinda parametre verdik burada kullandik

                .then()
                .statusCode(200)
              //.log().body()

        ;
    }


    @Test
    public void queryParamTest() {

        // https://gorest.co.in/public/v1/users?page=1

        given()
                .param("page", 1)  // query param olunca sadece param diye yaziliyor
                .log().uri()  // request link

                .when()
                .get("https://gorest.co.in/public/v1/users")  // ?`page=1

                .then()
                .statusCode(200)
                .log().body()

        ;
    }


    @Test
    public void queryParamTest2() {

        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.

        for (int i = 1; i < 10; i++) {
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
    public void Setup() {

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
    public void requestResponseSpecification() {

        given()
                .param("page", 1)
                .spec(requestSpec)

                .when()
                .get("/users") // base URI oldugu icin sadece bunu yazdik. onden sabitledik

                .then()
                .spec(responseSpec)
        ;
    }

    @Test
    public void extractingJsonPath() {

        String countryName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        //.log().body()
                        .extract().path("country");

        System.out.println("countryName = " + countryName);
        Assert.assertEquals(countryName, "United States");
    }

    @Test
    public void extractingJsonPath2() {
        //placeName
        String placeName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().path("places[0].'place name'")  // places[0]['place name']
                ;

        System.out.println("placeName = " + placeName);
        Assert.assertEquals(placeName, "Beverly Hills");
    }

    @Test
    public void extractingJsonPath3() {
        // https://gorest.co.in/public/v1/users  dönen değerdeki limit bilgisini yazdırınız.

        int limit =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("meta.pagination.limit");

        System.out.println("limit = " + limit);
    }

    @Test
    public void extractingJsonPath4() {
        // https://gorest.co.in/public/v1/users  dönen değerdeki bütün idleri yazdırınız.

        List<Integer> idler =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .statusCode(200)
                        .extract().path("data.id"); // bütün id leri ver
        ;

        System.out.println("idler = " + idler);
    }

    @Test
    public void extractingJsonPath5() {
        // https://gorest.co.in/public/v1/users  dönen değerdeki bütün name lei yazdırınız.

        List<String> names =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .statusCode(200)
                        .extract().path("data.name"); // bütün id leri ver
        ;

        System.out.println("names = " + names);
    }


    @Test
    public void extractingJsonPathResponsAll() {
        //  tum datalari aliyoruz bu sekilde

        Response donenData =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .statusCode(200)
                        // .log().body()
                        .extract().response(); // dönen tüm datayı verir.
        ;

        List<Integer> idler = donenData.path("data.id");
        List<String> names = donenData.path("data.name");
        int limit = donenData.path("meta.pagination.limit");

        System.out.println("idler = " + idler);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue(names.contains("Dakshayani Pandey"));
        Assert.assertTrue(idler.contains(1203767));
        Assert.assertEquals(limit, 10, "test sonucu hatalı");
    }

    @Test
    public void extractJsonAll_POJO()
            // // POJO : JSON nesnesi : locationNesnesi
    {
        Location locationNesnesi=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        //.log().body()
                        .extract().body().as(Location.class)
                // // Location şablonuna
                ;

        System.out.println("locationNesnesi.getCountry() = " +
                locationNesnesi.getCountry());

        for(Place p: locationNesnesi.getPlaces())
            System.out.println("p = " + p);

        System.out.println(locationNesnesi.getPlaces().get(0).getPlacename());
    }

    @Test
    public void extractPOJO_Soru(){
        // aşağıdaki endpointte(link)  Dörtağaç Köyü ait diğer bilgileri yazdırınız

        Location adana=
                given()
                        .when()
                        .get("http://api.zippopotam.us/tr/01000")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().body().as(Location.class)
                ;

        for(Place p: adana.getPlaces())
            if (p.getPlacename().equalsIgnoreCase("Dörtağaç Köyü"))
            {
                System.out.println("p = " + p);
            }
    }

}





