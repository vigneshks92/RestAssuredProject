package apiChaining;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {

	Response response;
	String BaseURI = "http://localhost:3000/employees";

	@Test
	public void test1() {

		response = GetMethodAll();
		Assert.assertEquals(response.getStatusCode(), 200);

		response = PostMethod("Luchia", "2000");
		Assert.assertEquals(response.getStatusCode(), 201);
		JsonPath Jpath = response.jsonPath();
		int EmpId = Jpath.get("id");
		System.out.println("id:-" + EmpId);

		response = PutMethod(EmpId, "Rick", "6000");
		Assert.assertEquals(response.getStatusCode(), 200);
		Jpath = response.jsonPath();
		Assert.assertEquals(Jpath.get("name"), "Rick");

		response = DeleteMethod(EmpId);
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBody().asString(), "{}");

		response = GetMethod(EmpId);
		Assert.assertEquals(response.getStatusCode(), 404);
		Assert.assertEquals(response.getBody().asString(), "{}");

	}

	public Response GetMethodAll() {

		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.get();

		return response;
	}

	public Response PostMethod(String Name, String Salary) {

		RestAssured.baseURI = BaseURI;

		JSONObject jobj = new JSONObject();
		jobj.put("name", Name);
		jobj.put("salary", Salary);

		RequestSpecification request = RestAssured.given();

		Response response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(jobj.toString())
				.post("/create");

		return response;
	}

	public Response PutMethod(int EmpId, String Name, String Salary) {

		RestAssured.baseURI = BaseURI;

		JSONObject jobj = new JSONObject();
		jobj.put("name", Name);
		jobj.put("salary", Salary);

		RequestSpecification request = RestAssured.given();

		Response response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(jobj.toString())
				.put("/" + EmpId);

		return response;
	}

	public Response DeleteMethod(int EmpId) {

		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.delete("/" + EmpId);

		return response;

	}

	public Response GetMethod(int EmpId) {

		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.get("/" + EmpId);

		return response;
	}

}
