package api.test;

import api.endpoints.UserEndPoints;
import api.payload.User;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserTests {

    Faker faker;
    User userPayload;

    @BeforeClass
    public void setUp() {
        faker = new Faker();
        userPayload = new User();

        userPayload.setId(faker.idNumber().hashCode());
        userPayload.setUsername(faker.name().username());
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setEmail(faker.internet().emailAddress());
        userPayload.setPassword(faker.internet().password(5, 10));
        userPayload.setPhone(faker.phoneNumber().cellPhone());
        userPayload.setUserStatus(0);
    }

    @Test(priority = 1)
    public void testPostUser() {
        Response response = UserEndPoints.createUser(userPayload);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testGetUserByName() throws InterruptedException {
        System.out.println("username: " + this.userPayload.getUsername());
        Thread.sleep(1000);
        Response response = UserEndPoints.readUser(this.userPayload.getUsername());
        response.then().log().all();

        System.out.println("User Name"+response.jsonPath().get("username"));
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3)
    public void testUpdateUserByName() {
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setEmail(faker.internet().safeEmailAddress());

        System.out.println("updated FirstName: " + userPayload.getFirstName());
        System.out.println("updated LastName: " + userPayload.getLastName());
        System.out.println("updated Email: " + userPayload.getEmail());

        Response response = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);

        Response responseAfterUpdate = UserEndPoints.readUser(this.userPayload.getUsername());

        System.out.println("updated FirstName: " + responseAfterUpdate.jsonPath().get("firstName"));
        System.out.println("updated LastName: " + responseAfterUpdate.jsonPath().get("lastName"));
        System.out.println("updated Email: " + responseAfterUpdate.jsonPath().get("email"));

//        Assert.assertEquals(responseAfterUpdate.jsonPath().get("firstName"), userPayload.getFirstName());
//        Assert.assertEquals(responseAfterUpdate.jsonPath().get("lastName"), userPayload.getLastName());
//        Assert.assertEquals(responseAfterUpdate.jsonPath().get("email"), userPayload.getEmail());
    }

    @Test(priority = 4)
    public void testDeleteUserByName() throws InterruptedException {
        Thread.sleep(1000);
        Response response = UserEndPoints.deleteUser(this.userPayload.getUsername());
        System.out.println("Delete User Response: " + response.asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        Response responseAfterDelete = UserEndPoints.readUser(this.userPayload.getUsername());
        Assert.assertEquals(responseAfterDelete.getStatusCode(), 404);
    }

}
