package GoRest;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTest {

    Faker randomUretici=new Faker();
    int userID;

    @Test(enabled = false)
    public void createUserJson(){

        // POST https://gorest.co.in/public/v2/users
        // "Authorization: Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d"
        // {"name":"{{$randomFullName}}", "gender":"male", "email":"{{$randomEmail}}", "status":"active"}



        String rndFullName=randomUretici.name().fullName();
        String rndEmail=randomUretici.internet().emailAddress();

        userID=
                given()
                        .header("Authorization","Bearer af9f652d58c653ec36210debee67541bce2ef6bf5466cae735e1a09562282daa")
                        .contentType(ContentType.JSON) // gönderilecek data JSON
                        .body("{\"name\":\""+rndFullName+"\", \"gender\":\"male\", \"email\":\""+rndEmail+"\", \"status\":\"active\"}")
                        //.log().uri()
                        //.log().body()

                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
                ;
    }

    @Test
    public void createUserMap() {
        String rndFullname = randomUretici.name().fullName();
        String rndEmail = randomUretici.internet().emailAddress();

        Map<String,String> newUser=new HashMap<>();
        newUser.put("name",rndFullname);
        newUser.put("gender","male");
        newUser.put("email",rndEmail);
        newUser.put("status","active");

        userID =
                given()
                        .header("Authorization", "Bearer af9f652d58c653ec36210debee67541bce2ef6bf5466cae735e1a09562282daa")
                        .contentType(ContentType.JSON) // gönderilecek data JSON
                        .body(newUser)
                        //.log().uri()
                        //.log().body()

                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
    }

    @Test(enabled = false)
    public void createUserClass() {
        String rndFullname = randomUretici.name().fullName();
        String rndEmail = randomUretici.internet().emailAddress();

        User newUser=new User();
        newUser.name=rndFullname;
        newUser.gender="male";
        newUser.email=rndEmail;
        newUser.status="active";

        userID =
                given()
                        .header("Authorization", "Bearer af9f652d58c653ec36210debee67541bce2ef6bf5466cae735e1a09562282daa")
                        .contentType(ContentType.JSON) // gönderilecek data JSON
                        .body(newUser)
                        //.log().uri()
                        //.log().body()

                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
    }




    @Test(dependsOnMethods = "createUserMap")
    public void getUserByID(){

        given()
                .header("Authorization", "Bearer af9f652d58c653ec36210debee67541bce2ef6bf5466cae735e1a09562282daa")

                .when()
                .get("https://gorest.co.in/public/v2/users/"+userID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id",equalTo(userID))
        ;


    }

    @Test
    public void updateUser(){


    }

    @Test
    public void deleteUser(){


    }


}
