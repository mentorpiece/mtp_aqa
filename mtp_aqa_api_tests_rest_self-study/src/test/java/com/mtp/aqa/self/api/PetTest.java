package com.mtp.aqa.self.api;

import com.mtp.aqa.self.api.misc.model.PetDto;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.mtp.aqa.self.api.misc.Constants.BASE_URL;
import static io.restassured.RestAssured.given;
import static java.util.stream.Collectors.toSet;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * A set of tests to cover the Pet resource.
 */
class PetTest {

    private static final String PET_URL = "/pet";
    private static final String BY_STATUS_URL = PET_URL + "/findByStatus";
    private static final String STATUS_PARAM = "status";

    @DisplayName("Get pet by status")
    @Test //TODO(Student): transform into data-driven.
    public void testFindPetByStatus() {
        // Arrange
        var targetStatus = "available";

        // Act
        var response = given()
                .baseUri(BASE_URL)
                .queryParam(STATUS_PARAM, targetStatus)
                .when()
                .log().all()
                .get(BY_STATUS_URL)
                .then()
                .log().all()
                .statusCode(SC_OK)
                .extract().as(new TypeRef<List<PetDto>>() {
                });

        // Assert
        assertThat(response)
                .as("GET-PET-BY-STATUS response should not be empty.")
                .isNotEmpty();
        var actualStatuses = response.stream().map(PetDto::getStatus).collect(toSet());
        assertThat(actualStatuses)
                .as("Seems GET-PET-BY-STATUS response contained more than one status.")
                .hasSize(1);
        assertThat(actualStatuses.stream().findFirst().get())
                .as("Seems GET-PET-BY-STATUS response contained unexpected status.")
                .isEqualTo(targetStatus);
    }
}