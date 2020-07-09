package script;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ScriptJson {

	Response response;
	
	@BeforeClass
	public static void init() {
		RestAssured.baseURI = "https://api.trello.com/1/actions/592f11060f95a3d3d46a987a";
		//RestAssured.basePath = "/data";
		RestAssured.basePath = "/list";
	}
	
	
	@Test
	public void getValorEspecifico() {
		String list = RestAssured.given()
				.log()
				.all()
				.when()
				.get("/name")
				.asString();
		System.out.println(list);
	}
	
	
	public void enviarArquivoJson() throws IOException {

		try {
			FileInputStream arqJson = new FileInputStream(new File("./testeJSON.json"));

			//COLOCAR URL API VIA POST
			given()
			.header("Content-Type","application/json")
			.and()
				.body(IOUtils.toString(arqJson,"UTF-8"))
			.when()
				.post("/posts")
			.then()
				.statusCode(201)
			.and()
			.log().all();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Lista todos as informacoes
	 * */
	//@Test
	public void getAllBodyJSON() {

		RequestSpecification http = RestAssured.given();
		response = http.request(Method.GET);

		String responseBody = response.getBody().asString();
		System.out.println("INFO JSON "+ responseBody);

		int statusJson = response.getStatusCode();
		System.out.println("STATUS CONEXAO JSON " + statusJson );
		Assert.assertEquals(statusJson, 200);

		String statusHTTP = response.getStatusLine();
		System.out.println("Status line is: " + statusHTTP);
		Assert.assertEquals(statusHTTP, "HHTP/1,1 200 OK");
	}
	
	


	/*
	 * CRIAR UMA STRING DO FORMATO JSON PARA ENVIAR PARA API
	 * USANDO O ARQUIVO FEATURE DO CUCUMBER
	 * */
	
	public void postJSON() {

		RequestSpecification http = RestAssured.given();

		JSONObject jSONObject = new  JSONObject();

		jSONObject.put("Nome", "");
		jSONObject.put("CPF", "");

		http.header("Content-Type","application/json");
		http.body(jSONObject.toString());

		response = http.request(Method.POST,"/pessoa");
		String responseBody = response.getBody().asString();
		System.out.println("INFO JSON "+ responseBody);

		int statusJson = response.getStatusCode();
		System.out.println("STATUS CONEXAO JSON " + statusJson );
		Assert.assertEquals(statusJson, 201);

		//CASO TENHA UMA MENSAGEM DE RETORNO
		String msnRetorno = response.jsonPath().get("SUCESSO");
		Assert.assertEquals(msnRetorno, "SUCESSO");

	}


	public void updateJson() throws InterruptedException {

		RequestSpecification http = RestAssured.given();
		JSONObject jSONObject = new  JSONObject();

		jSONObject.put("Nome", "teste");
		jSONObject.put("CPF", "teste");
		
		http.header("Content-Type","application/json");
		http.body(jSONObject.toString());
		response = http.request(Method.PUT,"/pessoa/{111}/1");
		Thread.sleep(5);
		
	}
	
	public void excluirJSON() throws InterruptedException {
		
		RequestSpecification http = RestAssured.given();
		http.header("Content-Type","application/json");
		response = http.request(Method.GET,"/pessoa");
	}
}