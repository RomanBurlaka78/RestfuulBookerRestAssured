package api.tests.smoke;

import api.pojo.GetBookingDates;
import api.pojo.GetToken;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

public class SmokeTest {
    final String TOKEN = "{\n" +
            "    \"username\" : \"admin\",\n" +
            "    \"password\" : \"password123\"\n" +
            "}";
    final String CREATE_BOOKING = "{\n" +
            "    \"firstname\" : \"Jim\",\n" +
            "    \"lastname\" : \"Brown\",\n" +
            "    \"totalprice\" : 111,\n" +
            "    \"depositpaid\" : true,\n" +
            "    \"bookingdates\" : {\n" +
            "        \"checkin\" : \"2018-01-01\",\n" +
            "        \"checkout\" : \"2019-01-01\"\n" +
            "    },\n" +
            "    \"additionalneeds\" : \"Breakfast\"\n" +
            "}";
    final String UPDATE_BOOKING = "{\n" +
            "    \"firstname\" : \"Roman\",\n" +
            "    \"lastname\" : \"Burlaka\",\n" +
            "    \"totalprice\" : 1122,\n" +
            "    \"depositpaid\" : true,\n" +
            "    \"bookingdates\" : {\n" +
            "        \"checkin\" : \"2024-10-12\",\n" +
            "        \"checkout\" : \"2024-10-14\"\n" +
            "    },\n" +
            "    \"additionalneeds\" : \"Alll\"\n" +
            "}";

    @Test
    public void testGetAllBooking() {
        Response response = given()
                .log().all()
                .get("https://restful-booker.herokuapp.com/booking")
                .then().log().all()
                .statusCode(200)
                .extract().response();
        System.out.println(response.asString());

        Assert.assertEquals(response.asString().contains("bookingid"), true);

    }

    @Test
    public void testGetBookingUser() {
        GetBookingDates response = given()
                .log().all()
                .accept("application/json")
                .when()
                .get("https://restful-booker.herokuapp.com/booking/1")
                .then()
                .log().all()
                .statusCode(200)
                .extract().as(GetBookingDates.class);
        assertThat(response.getFirstname() == "Mary");
        assertThat(response.getLastname() == "Jackson");

    }

    @Test
    public void testCreatePost() {
        GetBookingDates response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept("application/json")
                .when()
                .body(CREATE_BOOKING)
                .post(" https://restful-booker.herokuapp.com/booking")
                .then().log().all()
                .statusCode(200)
                .extract().as(GetBookingDates.class);
        System.out.println("BookingId : " + response.getBookingid());
        assertThat(response.getFirstname() == "Jim");
        assertThat(response.getLastname() == "Brown");


    }

    @Test
    public void testGetToken() {
        GetToken token = given()
                .filter(new AllureRestAssured())
                .log().all()
                .contentType(ContentType.JSON)
                .body(TOKEN)
                .filter(new AllureRestAssured())
                .post("https://restful-booker.herokuapp.com/auth")
                .then()
                .log().all()
                .extract().as(GetToken.class);

        assertThat(!token.getToken().isEmpty());
    }

    @Test
    public void testUpdate() {
        GetToken token = given()
                .filter(new AllureRestAssured())
                .log().all()
                .contentType(ContentType.JSON)
                .body(TOKEN)
                .post("https://restful-booker.herokuapp.com/auth")
                .then()
                .log().all()
                .extract().as(GetToken.class);

        GetBookingDates createBooking = given()
                .filter(new AllureRestAssured())
                .log().all()
                .contentType(ContentType.JSON)
                .accept("application/json")
                .when()
                .body(CREATE_BOOKING)
                .post(" https://restful-booker.herokuapp.com/booking")
                .then().log().all()
                .statusCode(200)
                .extract().as(GetBookingDates.class);


        GetBookingDates updateBookingPut = given()
                .filter(new AllureRestAssured())
                .log().all()
                .contentType(ContentType.JSON)
                .accept("application/json")
                .cookies("token", token.getToken())
                .when()
                .body(UPDATE_BOOKING)
                .put(" https://restful-booker.herokuapp.com/booking/" + createBooking.getBookingid())
                .then().log().all()
                .statusCode(200)
                .extract().as(GetBookingDates.class);

        assertThat(updateBookingPut.getFirstname() == "Roman");
        assertThat(updateBookingPut.getBookingid() == createBooking.getBookingid());


    }

    @Test
    public void testDeleteBooking() {
        GetToken tokenBooking = given()
                .filter(new AllureRestAssured())
                .log().all()
                .contentType(ContentType.JSON)
                .body(TOKEN)
                .post("https://restful-booker.herokuapp.com/auth")
                .then()
                .log().all()
                .extract().as(GetToken.class);

        assertThat(!tokenBooking.getToken().isEmpty());

        GetBookingDates createBooking = given()
                .filter(new AllureRestAssured())
                .log().all()
                .contentType(ContentType.JSON)
                .accept("application/json")
                .when()
                .body(CREATE_BOOKING)
                .post(" https://restful-booker.herokuapp.com/booking")
                .then().log().all()
                .statusCode(200)
                .extract().as(GetBookingDates.class);

        assertThat(createBooking.getBookingid().intValue());

        Response delete = given()
                .filter(new AllureRestAssured())
                .log().all()
                .contentType(ContentType.JSON)
                .accept("application/json")
                .cookies("token", tokenBooking.getToken())
                .when()
                .delete("https://restful-booker.herokuapp.com/booking/" + createBooking.getBookingid())
                .then().log().all()
                .extract().response();

        assertThat(delete.statusCode() == 201);

    }
}
