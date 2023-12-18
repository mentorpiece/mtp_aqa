package com.mtp.aqa.self.api;

import com.mtp.aqa.self.api.misc.model.ApiResponse;
import com.mtp.aqa.self.api.misc.model.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.mtp.aqa.self.api.misc.Constants.BASE_URL;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * A set of tests to cover the User resource.
 */
class UserTest {

    private static final String USER_URL = "/user";
    private static final String USER_BY_USERNAME_URL = USER_URL + "/{username}";

    @DisplayName("Create user, then read the user's data to check.")
    @Test
    public void testCreateUser() {
        // Arrange
        var userToCreate = new UserDto();
        userToCreate.setUsername("VaTt" + Math.random());// Random values for other fields?
        userToCreate.setFirstName("Vasja");
        userToCreate.setLastName("Testkin");
        userToCreate.setEmail("VT@example.com");
        userToCreate.setPassword("Bez_parolja");
        userToCreate.setPhone("+0987654321");
        userToCreate.setUserStatus(0);

        // Act
        var createResponse = given()
                .baseUri(BASE_URL)
                .when().log().all()
                .contentType("application/json")
                .body(userToCreate)
                .post(USER_URL)
                .then().log().all()
                .statusCode(SC_OK)
                .extract().as(ApiResponse.class);

        // Assert creation
        assertThat(createResponse.getMessage())
                .as("CREATE-USER response should contain some data(id) in the 'message' field.")
                .isNotEmpty();

        // Build expected user data
        var userIdAsString = createResponse.getMessage();
        userToCreate.setId(Long.parseLong(userIdAsString));

        // Read user data using the username
        var actualUserData = given()
                .baseUri(BASE_URL).log().all()
                .contentType("application/json")
                .get(USER_BY_USERNAME_URL, userToCreate.getUsername()) //Path-param!
                .then().log().all()
                .statusCode(SC_OK)
                .extract().as(UserDto.class);

        // Compare actual and expected user data.
        assertThat(actualUserData)
                .as("Seems GET-USER returned unexpected data for just create user.")
                .isEqualTo(userToCreate);
    }
}