package api.tests.base;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {
    static Faker faker = new Faker();
    protected String firstName;
    protected String lastName;

    public String getFirstName() {
        return firstName = faker.name().firstName();
    }

    public String getLastName() {
        return lastName = faker.name().lastName();

    }

    @BeforeClass
    public void beforeClass() {
        setupRequestSpecification();
        installSpecifications();

    }

    public RequestSpecification setupRequestSpecification()
    {
        return new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setContentType(ContentType.JSON)
                .setBaseUri("https://restful-booker.herokuapp.com")
                .build();
    }
    public void installSpecifications() {
        RestAssured.requestSpecification = setupRequestSpecification();

    }


}
