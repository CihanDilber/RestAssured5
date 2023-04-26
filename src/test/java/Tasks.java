import Model.ToDo;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Tasks {

    /**

     Task 2
     create a request to https://httpstat.us/203
     expect status 203
     expect content type TEXT
     */

    @Test
    public void task2()
    {
        given()

                .when()
                .get("https://httpstat.us/203")

                .then()
                .log().all()
                .statusCode(203)
                .contentType(ContentType.TEXT)
        ;

    }

    /** Task 1

     create a request to https://jsonplaceholder.typicode.com/todos/2
     expect status 200
     Converting Into POJO*/

    @Test
    public void task1()
    {
        ToDo todo=
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().body().as(ToDo.class)
                ;

        System.out.println("todo = " + todo);
        System.out.println("todo.getTitle() = " + todo.getTitle());
    }

}
