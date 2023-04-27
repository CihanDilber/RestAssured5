package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountryTest {

    String countryID;   // lazim olacagi icin yukari aldik. Postman de Collections a almistik zaten
    RequestSpecification recSpec;

    @BeforeClass
    public void setup()  {
        baseURI="https://test.mersys.io";

        Map<String,String> userCredential=new HashMap<>();
        userCredential.put("username","turkeyts");
        userCredential.put("password","TechnoStudy123");
        userCredential.put("rememberMe","true");

        Cookies cookies=
        given()
                .contentType(ContentType.JSON)
                .body(userCredential)

                .when()
                .post("/auth/login")

                .then()
                .log().all()
                .statusCode(200)
                .extract().response().getDetailedCookies()
        ;

        recSpec= new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();
    }

    @Test
    public void createCountry()  {

        Faker faker=new Faker();

        Map<String,String> country=new HashMap<>();
        country.put("name",faker.address().country());
        country.put("code",faker.address().countryCode());

        countryID=
                given()
                        .spec(recSpec)
                        .body(country)
                        .log().body()

                        .when()
                        .post("/school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
        ;

        System.out.println("countryID = " + countryID);


    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative()  {  }

    @Test(dependsOnMethods = "createCountryNegative")
    public void updateCountry()  {  }

    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry()  {  }

    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative()  {  }

}


