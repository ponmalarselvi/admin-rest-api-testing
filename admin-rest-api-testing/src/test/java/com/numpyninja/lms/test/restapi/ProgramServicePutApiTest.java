package com.numpyninja.lms.test.restapi;

import static io.restassured.RestAssured.given;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.numpyninja.lms.test.util.ExcelUtils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ProgramServicePutApiTest {

	private static final String BASE_URL = "http://localhost:8080";

	@DataProvider
	public Object[][] getUpdateProgramApiData() throws Exception {

		Object[][] testObjArray = ExcelUtils
				.getDataFromSheet("src/test/resources/TestData/ProgramServicePostApiTestData.xlsx", "Sheet1");

		return (testObjArray);
	}

	@Test(dataProvider = "getUpdateProgramApiData")
	public void testUpdateProgram(String programName, String programDescription, String isOnline) {

		Reporter.log("Get program details from CreateProgram:" + programName);
		JSONObject request = new JSONObject();
		request.put("programName", programName);
		request.put("programDescription", programDescription);
		request.put("isOnline", isOnline);

		Response response = given().auth().basic("admin", "password").header("Content-type", "application/json").and().body(request).when()
				.post("/programs").then().extract().response();
		
		System.out.println("Response Body is: " + response.getBody().asString());

		JsonPath jsonPathEvaluator = response.jsonPath();

		// Then simply query the JsonPath object to get a String value of the node
		// specified by JsonPath: programId (Note: You should not put $. in the Java
		// code)
		Integer apiProgramId = jsonPathEvaluator.get("programId");
		
		request = new JSONObject();
		request.put("programName", programName + " Modified");
		request.put("programDescription", programDescription + " Modified");
		request.put("isOnline", isOnline);

		response = given().auth().basic("admin", "password").header("Content-type", "application/json")
				.and().body(request).when()
				.put("/programs/" + apiProgramId).then().extract().response();
		
		System.out.println("Response Body is: " + response.getBody().asString());

		 jsonPathEvaluator = response.jsonPath();

		// Then simply query the JsonPath object to get a String value of the node
		// specified by JsonPath: programId (Note: You should not put $. in the Java
		// code)
		String programNameUdt = jsonPathEvaluator.get("programName");

		// Let us print the city variable to see what we got
		Reporter.log("ProgramId received from Response " + apiProgramId);
		Assert.assertNotNull(apiProgramId);
		Assert.assertNotEquals(programName, programNameUdt);
	}

}
