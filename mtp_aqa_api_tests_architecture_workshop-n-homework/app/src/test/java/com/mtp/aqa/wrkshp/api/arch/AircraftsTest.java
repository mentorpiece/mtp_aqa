package com.mtp.aqa.wrkshp.api.arch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.ReplaceOperation;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.config.LogConfig;
import io.restassured.specification.RequestSpecification;
import lombok.val;
import net.datafaker.Faker;
import org.apache.http.HttpHeaders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static java.lang.System.out;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for the REST API /aircrafts resource related tests.
 */
public class AircraftsTest {
    /// Test groups:
    public static final String API = "API";
    public static final String API_REST = API + ".REST";
    public static final String API_REST_SMOKE = API_REST + ".smoke";
    public static final String API_REST_AIRCRAFTS = API_REST + ".aircrafts";

    // TODO(student): Shall be moved to a property file.
    // Test data (properties):
    public static final String BASE_URL = "http://localhost:8080/api/v0";
    public static final String ADMIN_NAME = "john";
    public static final String ADMIN_PASSWORD = "john123";

    // Aircraft resource paths:
    public static final String PATH = "aircrafts";
    public static final String PATH_DETAILS = PATH + "/{id}";
    public static final String PATH_MODELNAME = PATH + "/model/{modelName}";

    private Faker faker;
    private ObjectMapper objectMapper;
    private RequestSpecification reqSpec;

    public static String provideAlteredValue(String upd, String value) {
        var newValue = String.valueOf(value);
        if (newValue.startsWith(upd)) {
            newValue = newValue.replaceAll(upd, "");
        } else {
            newValue = upd + newValue;
        }
        return newValue;
    }

    public static <E> E getRandomItem(Random random, List<E> items) {
        checkSize(items);

        return items.get(random.nextInt(items.size()));
    }

    public static <E> E getRandomItem(List<E> items) {
        return getRandomItem(new Random(), items);
    }

    private static <E> void checkSize(List<E> items) {
        if (Objects.isNull(items) || items.isEmpty())
            throw new IllegalArgumentException("The list of items shall not be null or empty");
    }

    @BeforeClass(alwaysRun = true)
    public void setupAircraftsTests() {
        doBasicSuitePreconditionSteps();
        reqSpec = buildReqSpec();
        objectMapper = provideObjectMapper();
        faker = new Faker();
    }

    public void doBasicSuitePreconditionSteps() {
        RestAssured.config = RestAssured.config().logConfig(LogConfig
                .logConfig()
                .enablePrettyPrinting(true)
                .enableLoggingOfRequestAndResponseIfValidationFails()
                .blacklistHeader(HttpHeaders.AUTHORIZATION));

        RestAssured.authentication = basic(ADMIN_NAME, ADMIN_PASSWORD);
    }

    protected RequestSpecification buildReqSpec() {
        var headers = new HashMap<>(Map.of(
                HttpHeaders.CONTENT_TYPE, "application/json",
                "X-CUSTOM", "Testing" // Just an example of custom header usage
        ));

        return given().baseUri(BASE_URL)
                .log().all()
                .headers(headers);
    }

    @Test(description = "Create-read-update-delete an aircraft.",
            groups = {API, API_REST, API_REST_SMOKE, API_REST_AIRCRAFTS})
    public void createUpdateDeleteAircraftTest() throws JsonProcessingException {
        // Arrange - Create
        val inputData = CreateAircraftDto.of()
                .model(faker.aviation().aircraft())
                .manufacturer(faker.vehicle().manufacturer())
                // 116 - is a magic number and generally that is not good. Here - just an upper bound.
                .numberOfSeats(faker.random().nextInt(116) + 1) // #nextInt() could return 0.
                .build();
        val expectedDto = AircraftDto.of()
                .model(inputData.getModel())
                .manufacturer(inputData.getManufacturer())
                .numberOfSeats(inputData.getNumberOfSeats())
                .build();

        // Act - Create
        val createResponse = create(inputData);
        //[DISCUSS]: What if we run the test over an over again? What would we get in the DB in a year?

        // Assert the creation
        verifyCreateOrUpdateResponse(expectedDto, createResponse);
        // We could stop here but let's _read_ the entity via the API and check once again.
        val createdEntity = verifyCreationOrUpdateResult(expectedDto, createResponse.getId());

        // Arrange - Update
        val upd = "!UPD!";
        val newModel = provideAlteredValue(upd, createdEntity.getModel());
        val newManufacturer = provideAlteredValue(upd, createdEntity.getManufacturer());

        val altered = CreateAircraftDto.of()
                .model(newModel)
                .manufacturer(newManufacturer)
                .numberOfSeats(faker.random().nextInt(142) + 1) // #nextInt() could return 0.
                .build();

        // Act - Update
        val updateResponse = update(altered, createdEntity.getAircraftId());

        // Assert
        verifyCreateOrUpdateResponse(expectedDto, updateResponse);
        verifyCreationOrUpdateResult(expectedDto, updateResponse.getId());

        // Act - Delete
        val deleteResponse = delete(createdEntity.getAircraftId());

        // Assert - Delete
        assertThat(deleteResponse.getMessage())
                .as("Delete response's message should not be empty.")
                .isNotEmpty();
        assertThat(deleteResponse.getDetails())
                .as("Delete response's details should not be empty.")
                .isNotEmpty();
        assertThat(deleteResponse.getDetails().get("Id"))
                .as("Delete response's details should contain 'Id' key entry having %s value.", createdEntity.getAircraftId())
                .isNotEmpty()
                .isEqualTo(String.valueOf(createdEntity.getAircraftId()));
        // TODO(student): Is that enough?
    }

    @Test(description = "Partially update a random aircraft.", groups = {API, API_REST, API_REST_AIRCRAFTS})
    public void patchAircraftTest() throws JsonProcessingException {
        // Arrange
        val original = getRandomItem(getAll());

        val newModel = provideAlteredValue("!PTCHD!", original.getModel());

        // See https://www.baeldung.com/spring-rest-json-patch
        val replaceOp = new ReplaceOperation(JsonPointer.of("model"), TextNode.valueOf(newModel));
        val patchData = new JsonPatch(List.of(replaceOp));
        val expectedDto = AircraftDto.of()
                .model(newModel)
                .manufacturer(original.getManufacturer())
                .numberOfSeats(original.getNumberOfSeats())
                .build();

        // Act
        val updateResponse = patch(patchData, original.getAircraftId());

        // Assert
        verifyCreateOrUpdateResponse(expectedDto, updateResponse);
        verifyCreationOrUpdateResult(expectedDto, updateResponse.getId());
    }

    // TODO: Could be made more generic, moved to a common location and re-used.
    private void verifyGetResponse(AircraftDto expected, AircraftDto actual) {
        out.println("Verify Get-Aircraft response.");
        assertThat(actual)
                .as("Seems Get-Aircraft call returned some unexpected data.")
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    private void verifyGetAllResponse(List<AircraftDto> expected, List<AircraftDto> actual) {
        out.println("Verify Get-All-Aircraft response.");
        assertThat(actual)
                .as("Seems Get-All-Aircrafts call returned some unexpected data.")
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    //("Verify Create-Aircrafts response.")
    private void verifyCreateOrUpdateResponse(AircraftDto expected, AircraftDto actual) {
        // TODO(student): Use a Logger!
        out.println("Verify Create/Update-Aircraft response.");
        verifyCreateResponseIgnoringSomeFields("Seems Create/Update-Aircraft call returned some unexpected data.",
                expected, actual);
        assertThat(actual.getAircraftId())
                .as("Seems Create/Update-Aircraft call returned some unexpected data - wrong id/aircraftId.")
                .isGreaterThan(0);
        assertThat(actual.getFlightIds())
                .as("Seems Create/Update-Aircraft call returned some unexpected data - Flights expected to be empty.")
                .isEmpty();
    }

    private void verifyCreateResponseIgnoringSomeFields(String note, AircraftDto expected, AircraftDto actual) {
        assertThat(actual)
                .as(note)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                // Ignore fields which are auto-generated or will be filled in later.
                .ignoringFields("id", "aircraftId", "flights")
                .isEqualTo(expected);
    }

    //("Verify the entity upon Create-Aircrafts call.")
    private AircraftDto verifyCreationOrUpdateResult(AircraftDto expected, long id) {
        out.printf("Verify creation/update result for %s.", id);

        val entityViaApi = getById(id);

        verifyCreateResponseIgnoringSomeFields("Seems wrong data was saved to DB upon Create/Update-Aircraft API call.",
                expected, entityViaApi);

        return entityViaApi;
    }

    private void verifyDeleteResult(long deletedRecordId, ApiResponse actualResponse) {
        assertThat(actualResponse.getMessage())
                .as("Delete response's message should not be empty.")
                .isNotEmpty();
        assertThat(actualResponse.getDetails())
                .as("Delete response's details should not be empty.")
                .isNotEmpty();
        assertThat(actualResponse.getDetails().get("Id"))
                .as("Delete response's details should contain 'Id' key entry having %s value.", deletedRecordId)
                .isNotEmpty()
                .isEqualTo(String.valueOf(deletedRecordId));
        // TODO(student): Is that enough?
    }

    public <R> R doPost(RequestSpecification reqSpec, int expectedStatus, Class<R> responseClass,
                        String body, String path, Object... pathParams) {
        out.printf("Sending POST for: %s %s | Body = %s%n", path, Arrays.toString(pathParams), body);
        return given()
                .spec(reqSpec)
                .body(body)
                .post(path, pathParams)
                .then().log().all()
                .and()
                .statusCode(expectedStatus)
                .extract().response().as(responseClass);
    }

    public <R> R doPost(RequestSpecification reqSpec, int expectedStatus, Class<R> responseClass,
                        Object body, String path, Object... pathParams) throws JsonProcessingException {
        return doPost(reqSpec, expectedStatus, responseClass,
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body),
                path, pathParams);
    }

    public <R> R doGet(RequestSpecification reqSpec, int expectedStatus, Class<R> responseClass, String path, Object... pathParams) {
        out.printf("Sending GET for: %s %s", path, Arrays.toString(pathParams));
        return given()
                .spec(reqSpec)
                .get(path, pathParams)
                .then().log().all()
                .and()
                .statusCode(expectedStatus)
                .extract().response().as(responseClass);
    }

    public <R> R doGet(RequestSpecification reqSpec, int expectedStatus,
                       TypeRef<R> responseTypeRef,
                       String path, Object... pathParams) {
        out.printf("Sending GET for: %s %s", path, Arrays.toString(pathParams));
        return given()
                .spec(reqSpec)
                .get(path, pathParams)
                .then().log().all()
                .and()
                .statusCode(expectedStatus)
                .extract().response().as(responseTypeRef);
    }

    public <R> R doPut(RequestSpecification reqSpec, int expectedStatus, Class<R> responseClass,
                       Object body, String path, Object... pathParams) throws JsonProcessingException {
        out.printf("Sending PUT for: %s %s |\n\t Body = %s", path, Arrays.toString(pathParams), body);
        return given()
                .spec(reqSpec)
                .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body))
                .put(path, pathParams)
                .then().log().all()
                .and()
                .statusCode(expectedStatus)
                .extract().response().as(responseClass);
    }

    public <R> R doPatch(RequestSpecification reqSpec, int expectedStatus, Class<R> responseClass,
                         Object body, String path, Object... pathParams) throws JsonProcessingException {
        out.printf("Sending PATCH for: %s %s |\n\t Body = %s", path, Arrays.toString(pathParams), body);
        return given()
                .spec(reqSpec)
                .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body))
                .patch(path, pathParams)
                .then().log().all()
                .and()
                .statusCode(expectedStatus)
                .extract().response().as(responseClass);
    }

    public <R> R doDelete(RequestSpecification reqSpec, int expectedStatus, Class<R> responseClass, String path, Object... pathParams) {
        out.printf("Sending DELETE for: %s %s ", path, Arrays.toString(pathParams));
        return given()
                .spec(reqSpec)
                .delete(path, pathParams)
                .then().log().all()
                .and()
                .statusCode(expectedStatus)
                .extract().response().as(responseClass);
    }

    public <R> R get(String path, String pathParam, int expectedStatusCode, Class<R> responseClass) {
        return doGet(reqSpec, expectedStatusCode, responseClass, path, pathParam);
    }

    public AircraftDto getById(long id) {
        out.printf("Get aircraft by id=%s", id);
        return get(PATH_DETAILS, String.valueOf(id), SC_OK, AircraftDto.class);
    }

    public List<AircraftDto> getAll() {
        out.println("Get ALL aircrafts");
        return doGet(reqSpec, SC_OK, new TypeRef<>() {
        }, PATH);
    }

    public List<AircraftDto> getAllByModelName(String modelName) {
        out.printf("Get aircraft by modelName=%s", modelName);
        return doGet(reqSpec, SC_OK, new TypeRef<>() {
        }, PATH_MODELNAME, modelName);
    }

    public AircraftDto create(CreateAircraftDto data) throws JsonProcessingException {
        out.println("Call to create an aircraft: " + data);
        return doPost(reqSpec, SC_CREATED, AircraftDto.class, data, PATH);
    }

    public AircraftDto update(CreateAircraftDto data, long id) throws JsonProcessingException {
        out.printf("Call to update aircraft %s: \n%s", id, data);
        return doPut(reqSpec, SC_CREATED, AircraftDto.class, data, PATH_DETAILS, id);
    }

    // https://www.baeldung.com/spring-rest-json-patch
    public AircraftDto patch(JsonPatch patch, long id) throws JsonProcessingException {
        out.printf("Call to partially update(patch) aircraft %s: \n%s", id, patch);
        return doPatch(reqSpec, SC_CREATED, AircraftDto.class, patch, PATH_DETAILS, id);
    }

    public ApiResponse delete(long id) {
        out.printf("Call to delete aircraft %s", id);
        return doDelete(reqSpec, SC_OK, ApiResponse.class, PATH_DETAILS, id);
    }

    private ObjectMapper provideObjectMapper() {
        val om = new ObjectMapper();
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.setDateFormat(new StdDateFormat().withColonInTimeZone(true));

        return om;
    }
}
