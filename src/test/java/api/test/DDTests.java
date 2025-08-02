package api.test;

import api.endpoints.UserEndPoints;
import api.payload.User;
import api.utilities.DataProviders;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DDTests {

    @Test(priority = 1, dataProvider ="Data", dataProviderClass = DataProviders.class)
    public void testPostUser(String userID, String userName, String firstName, String lastName, String email, String password, String phone) {
        User userPayLoad=new User();

        userPayLoad.setId(Integer.parseInt(userID));
        userPayLoad.setUsername(userName);
        userPayLoad.setFirstName(firstName);
        userPayLoad.setLastName(lastName);
        userPayLoad.setEmail(email);
        userPayLoad.setPassword(password);
        userPayLoad.setPhone(phone);

        Response response = UserEndPoints.createUser(userPayLoad);
        Assert.assertEquals(response.getStatusCode(), 200);

    }

    @Test(priority = 2, dataProvider ="userNames", dataProviderClass = DataProviders.class)
    public void testDeleteUser(String userName) {
        Response response = UserEndPoints.deleteUser(userName);
        System.out.println("Delete User Response: " + response.asString());
        Assert.assertEquals(response.getStatusCode(), 200);

    }


}
