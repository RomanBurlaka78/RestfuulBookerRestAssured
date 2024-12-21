package api.tests.smoke;

import api.pojo.GetBookingDates;
import api.pojo.GetToken;
import api.tests.base.BaseTest;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class SmokeTest extends BaseTest {
    final static String TOKEN = "{\n" +
            "    \"username\" : \"admin\",\n" +
            "    \"password\" : \"password123\"\n" +
            "}";

    final String CREATE_BOOKING = "{\n" +
            "    \"firstname\" : \"" + getFirstName()  +"\",\n" +
            "    \"lastname\" : \"" + getLastName() + "\",\n" +
            "    \"totalprice\" : 111,\n" +
            "    \"depositpaid\" : true,\n" +
            "    \"bookingdates\" : {\n" +
            "        \"checkin\" : \"2018-01-01\",\n" +
            "        \"checkout\" : \"2019-01-01\"\n" +
            "    },\n" +
            "    \"additionalneeds\" : \"Breakfast\"\n" +
            "}";
    final String UPDATE_BOOKING = "{\n" +
            "    \"firstname\" : \"" + firstName + "\",\n" +
            "    \"lastname\" : \"" + lastName  +"\",\n" +
            "    \"totalprice\" : 1122,\n" +
            "    \"depositpaid\" : true,\n" +
            "    \"bookingdates\" : {\n" +
            "        \"checkin\" : \"2024-10-12\",\n" +
            "        \"checkout\" : \"2024-10-14\"\n" +
            "    },\n" +
            "    \"additionalneeds\" : \"Alll\"\n" +
            "}";

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


    @Test(groups = {"positive"})
    public void testGetAllBooking() {
        Response response = given()
                .get("/booking")
                .then().log().all()
                .statusCode(200)
                .extract().response();


        Assert.assertEquals(response.asString().contains("bookingid"), true);

    }


    @Test(groups = {"positive"})
    public void testGetBookingUser() {
        GetBookingDates response = given()
                .when()
                .get("https://restful-booker.herokuapp.com/booking/1")
                .then()
                .log().all()
                .statusCode(200)
                .extract().as(GetBookingDates.class);
        assertThat(response.getFirstname() == "Mary");
        assertThat(response.getLastname() == "Jackson");

    }

    @Test(groups = {"positive"})
    public void testCreatePost() {
        GetBookingDates createBooking = given()
                .accept("application/json")
                .when()
                .body(CREATE_BOOKING)
                .post(" https://restful-booker.herokuapp.com/booking")
                .then().log().all()
                .statusCode(200)
                .extract().as(GetBookingDates.class);

        assertThat(createBooking.getFirstname() == firstName);
        assertThat(createBooking.getLastname() == lastName);


    }

    @Test(groups = {"positive"})
    public String testGetToken() {
        GetToken token = given()
                .filter(new AllureRestAssured())
                .body(TOKEN)
                .filter(new AllureRestAssured())
                .post("https://restful-booker.herokuapp.com/auth")
                .then()
                .log().all()
                .extract().as(GetToken.class);

        assertThat(!token.getToken().isEmpty());

        return token.getToken();


    }

    @Test()
    public void testUpdate() {

        GetBookingDates updateBookingPut = given()
                .filter(new AllureRestAssured())
                .log().all()
                .cookies("token", token.getToken())
                .when()
                .body(UPDATE_BOOKING)
                .put(" https://restful-booker.herokuapp.com/booking/" + createBooking.getBookingid())
                .then().log().all()
                .statusCode(200)
                .extract().as(GetBookingDates.class);

        assertThat(updateBookingPut.getFirstname().matches(firstName));
        assertThat(updateBookingPut.getBookingid() == createBooking.getBookingid());


    }

    @Test(groups = {"positive"})
    public void testDeleteBooking() {


        Response delete = given()
                .filter(new AllureRestAssured())
                .accept("application/json")
                .cookies("token", token.getToken())
                .when()
                .delete("https://restful-booker.herokuapp.com/booking/" + createBooking.getBookingid())
                .then().log().all()
                .extract().response();

        assertThat(delete.statusCode() == 201);

    }



}
