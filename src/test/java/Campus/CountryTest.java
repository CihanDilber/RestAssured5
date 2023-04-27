package Campus;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountryTest {


    RequestSpecification recSpec;

    @BeforeClass
    public void Login()  {
        baseURI="https://test.mersys.io";

        Map<String,String> userCredential=new HashMap<>();
        userCredential.put("username","turkeyts");
        userCredential.put("password","TechnoStudy123");
        userCredential.put("rememberMe","true");

        given()
                .contentType(ContentType.JSON)
                .body(userCredential)

                .when()
                .post("/auth/login")

                .then()
                .log().all()
                .statusCode(200)
        ;

        recSpec= new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void createCountry()  {  }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative()  {  }

    @Test(dependsOnMethods = "createCountryNegative")
    public void updateCountry()  {  }

    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry()  {  }

    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative()  {  }

}


