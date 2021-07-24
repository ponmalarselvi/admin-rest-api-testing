package com.numpyninja.lms.test.restapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.numpyninja.lms.test.util.ExcelUtils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ProgramServiceGetApiTest {

	private static final String BASE_URL = "http://localhost:8080";

	@DataProvider
	public Object[][] getProgramApiData() throws Exception {

		Object[][] testObjArray = ExcelUtils.getDataFromSheet("src/test/resources/TestData/ProgramServiceGetApiTestData.xlsx",
				"Sheet1");

		return (testObjArray);
	}

	@Test
	public void testGetAllPrograms() {
		Response response = given().auth().basic("admin", "password").when().get(BASE_URL + "/programs"); // This will
		Assert.assertEquals(response.getStatusCode(), 200, "Response received successfully");

		String programId = response.jsonPath().getString("programId[0]");
		Reporter.log("First retrieved program id: " + programId);
		Assert.assertNotNull(programId);

	}

	@Test(dataProvider = "getProgramApiData")
	public void testFindProgram(String programId, String programName, String programDescription,
			String isOnline) {

		Reporter.log("Get program details from programs Api for program id" + programId);

		Response response = given().auth().basic("admin", "password").when()
				.get("/programs/" + programId);

		// First get the JsonPath object instance from the Response interface
		JsonPath jsonPathEvaluator = response.jsonPath();

		// Then simply query the JsonPath object to get a String value of the node
		// specified by JsonPath: programId (Note: You should not put $. in the Java
		// code)
		Integer apiProgramId = jsonPathEvaluator.get("programId");

		// Let us print the city variable to see what we got
		Reporter.log("ProgramId received from Response " + apiProgramId);

		// Validate the response
		Assert.assertNotNull(apiProgramId);
		Assert.assertTrue(Integer.parseInt(programId) == apiProgramId);

	}

}
