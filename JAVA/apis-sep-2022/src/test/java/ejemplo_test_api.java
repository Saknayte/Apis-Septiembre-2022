import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;



public class ejemplo_test_api {
    @Test
    public void api_test(){
        //URLvy URI
        RestAssured.baseURI = String.format("https://reqres.in/api/users?page=2");
        Response response = (Response) given().log().all().headers("Accept", "*/*").get();

        String body_response = response.getBody().prettyPrint();
        System.out.println(body_response);
        String responseCode = String.format(String.valueOf(response.getStatusCode()));
        System.out.println("Status code: " + responseCode);

        //JUnit 5 (al menos 5 pruebas)
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacío
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("id"));

        //Validar tiempo de respuesta
        long tiempo =  response.getTime();
        assertTrue(tiempo < 800);

        //Validar los headers
        String headers_response = response.getHeaders().toString();
        System.out.println("Las cabeceras: " + headers_response);
        assertTrue(headers_response.contains("application/json"));

    }

    @Test
    public void add_pet_test (){
        RestAssured.baseURI = String.format("https://petstore.swagger.io/v2/pet");
        String bodyRequest = "{\n" +
                "  \"category\": {\n" +
                "    \"id\": 6723439,\n" +
                "    \"name\": \"Perro de raza chica\"\n" +
                "  },\n" +
                "  \"name\": \"Yesenia\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"string\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"manchado\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";

        Response response = given()
                .log().all()
                .header("accept","application/json")
                .header("Content-Type", "application/json")
                .post(bodyRequest);

        String body_response = response.getBody().prettyPrint();
        //System.out.println("Cuerpo de respuesta", body_response);

    }
}
