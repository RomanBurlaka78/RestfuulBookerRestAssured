package api.tests;

import api.pojo.GetBookingDates;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;


import static io.restassured.RestAssured.given;


public class GetBookingIdsTest {

    @Test
    public void testGetAllBooking() {
        Response response = given()
                .filter(new AllureRestAssured())
                .log().all()
                .get("https://restful-booker.herokuapp.com/booking")
                .then().log().all()
                .statusCode(200)
                .extract().response();
        System.out.println(response);

        Assert.assertEquals(response.asString().contains("bookingid"), true);

    }

    @Test
    public void testGetBookingId1() {
        GetBookingDates response = given()
                .filter(new AllureRestAssured())
                .log().all()
                .when()
                .accept("application/json")
                .contentType(ContentType.JSON)
                .get("https://restful-booker.herokuapp.com/booking/1")
                .then().log().all()
                .statusCode(200)
                .extract().as(GetBookingDates.class);

        assertThat(response.getFirstname().matches("Jim"));

    }
    @Test
    public void testGetBookingId2() {
        GetBookingDates response = given()
                .filter(new AllureRestAssured())
                .log().all()
                .when()
                .accept("application/json")
                .contentType(ContentType.JSON)
                .get("https://restful-booker.herokuapp.com/booking/2")
                .then().log().all()
                .statusCode(200)
                .extract().as(GetBookingDates.class);

        assertThat(!response.getFirstname().isEmpty());
        assertThat(response.getLastname().equalsIgnoreCase("Wilson"));

    }




}
