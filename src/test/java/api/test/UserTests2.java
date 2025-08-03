package api.test;

import api.endpoints.UserEndPoints2;
import api.payload.User;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserTests2 {

    Faker faker;
    User userPayload;

    public Logger logger;

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

        logger = org.apache.logging.log4j.LogManager.getLogger(this.getClass());
    }

    @Test(priority = 1)
    public void testPostUser() {
        logger.info("********* Creating User *********");
        Response response = UserEndPoints2.createUser(userPayload);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("********** User Created Successfully *********");
    }

    @Test(priority = 2)
    public void testGetUserByName() throws InterruptedException {
        logger.info("********* Getting User Info *********");
        System.out.println("username: " + this.userPayload.getUsername());
        Thread.sleep(1000);
        Response response = UserEndPoints2.readUser(this.userPayload.getUsername());
        response.then().log().all();

        System.out.println("User Name"+response.jsonPath().get("username"));
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("********** User Info Retrieved Successfully *********");
    }

    @Test(priority = 3)
    public void testUpdateUserByName() {
        logger.info("********* Updating User *********");
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setEmail(faker.internet().safeEmailAddress());

        System.out.println("updated FirstName: " + userPayload.getFirstName());
        System.out.println("updated LastName: " + userPayload.getLastName());
        System.out.println("updated Email: " + userPayload.getEmail());

        Response response = UserEndPoints2.updateUser(this.userPayload.getUsername(), userPayload);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);

        Response responseAfterUpdate = UserEndPoints2.readUser(this.userPayload.getUsername());
        Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);

//        System.out.println("updated FirstName: " + responseAfterUpdate.jsonPath().get("firstName"));
//        System.out.println("updated LastName: " + responseAfterUpdate.jsonPath().get("lastName"));
//        System.out.println("updated Email: " + responseAfterUpdate.jsonPath().get("email"));
//        System.out.println("userName: " + responseAfterUpdate.jsonPath().get("username"));

        Assert.assertEquals(responseAfterUpdate.jsonPath().get("firstName"), userPayload.getFirstName());
        Assert.assertEquals(responseAfterUpdate.jsonPath().get("lastName"), userPayload.getLastName());
        Assert.assertEquals(responseAfterUpdate.jsonPath().get("email"), userPayload.getEmail());

        logger.info("********** User Updated Successfully *********");

    }

    @Test(priority = 4)
    public void testDeleteUserByName() throws InterruptedException {
        logger.info("********* Deleting User *********");
        Thread.sleep(1000);
        Response response = UserEndPoints2.deleteUser(this.userPayload.getUsername()); // this.userPayload.getUsername()
        System.out.println("Delete User Response: " + response.asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        Response responseAfterDelete = UserEndPoints2.readUser(this.userPayload.getUsername()); //this.userPayload.getUsername()
        Assert.assertEquals(responseAfterDelete.getStatusCode(), 404);
        logger.info("********** User Deleted Successfully *********");
    }

}
