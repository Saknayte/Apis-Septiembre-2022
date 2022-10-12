import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import jdk.jfr.Name;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class eCommerce {
    //Variables
    static private String url_base = "webapi.segundamano.mx";
    static private String email = "test2022_agente@mailinator.com";
    static private String password = "54321";
    static private String access_token;
    static private String account_id;
    static private String uuid;

    static private String ad_id;

    static private String address_id;

    static private String id_alert;

   @Name("Obtener Token")
    private String obtener_Token(){

        RestAssured.baseURI=String.format("https://%s/nga/api/v1.1/private/accounts",url_base);

        Response response = given()
                .log().all()
                .queryParam("lang","es")
                .auth().preemptive().basic(email,password)
                .post();

        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Cambiar el body a JSon
        JsonPath jsonResponse = response.jsonPath();

        String accesstoken =jsonResponse.get("access_token");
        System.out.println("Token en funcion: " + accesstoken);
        access_token = accesstoken;

        //Otras variables
        String accountID =jsonResponse.get("account.account_id");
        System.out.println("account id en funcion: " + accountID);
        account_id = accountID;

        //Asignar la variable uuid
        String uid = jsonResponse.get("account.uuid");
        System.out.println("uuid en funcion: " + uid);
        uuid = uid;



        return access_token;

    }

    @Name("Obtener ad_idn")
    private String obtener_id() {

        RestAssured.baseURI = String.format("https://%s/v2/accounts/%s/up", url_base, uuid);

        String token = obtener_Token();
        System.out.println("Token: " + token);

        System.out.println("Token de funcion: " + access_token);
        System.out.println("account id de funcion: " + account_id);
        System.out.println("uuid de funcion: " + uuid);

        String body_request = "{\"category\":\"8143\"," +
                "\"subject\":\"dog one two \"," +
                "\"body\":\"dog one two, dog one two, dog one two \"," +
                "\"region\":\"4\"," +
                "\"municipality\":\"47\"," +
                "\"area\":\"35931\"," +
                "\"price\":\"1000\"," +
                "\"phone_hidden\":\"false\"," +
                "\"show_phone\":\"true\"," +
                "\"contact_phone\":\"5566778899\"}";

        //URL
        RestAssured.baseURI = String.format("https://%s/v2/accounts/%s/up", url_base, uuid);

        Response response = given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Accept", "*/*")
                .header("x-source", "PHOENIX_DESKTOP")
                .auth().preemptive().basic(uuid, access_token)
                .body((body_request))
                .post();

        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response);

        //Cambiar el body a JSon
        JsonPath jsonResponse = response.jsonPath();
        String adID = jsonResponse.get("data.ad.ad_id");
        System.out.println("ad_id en función: " + adID);
        ad_id = adID;

        return ad_id;
    }

    @Name("Obtener addressId")
    private String obtener_addressID() {

        String token = obtener_Token();
        System.out.println("Token: " + token);

        System.out.println("Token de funcion: " + access_token);
        System.out.println("account id de funcion: " + account_id);
        System.out.println("uuid de funcion: " + uuid);


        //ejecucion
        RestAssured.baseURI=String.format("https://%s/addresses/v1/create",url_base);

        Response response = given()
                .log().all()
                .formParam("contact","Agente de ventas")
                .formParam("phone","8776655443")
                .formParam("rfc","CAPL800101")
                .formParam("zipCode","45999")
                .formParam("exteriorInfo","Miguel Hidalgo 4232")
                .formParam("interiorInfo","2")
                .formParam("region","11")
                .formParam("municipality","300")
                .formParam("area","8094")
                .formParam("alias","La oficina")
                .header("Content-type","application/x-www-form-urlencoded")
                .header("Accept","application/json, text/plain, */*")
                .auth().preemptive().basic(uuid,access_token)
                .post();


        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );
        JsonPath jsonResponse = response.jsonPath();
        String address = jsonResponse.get("addressID");
        System.out.println("Address_ID en función: " + address);
        address_id = address;


       return address_id;
    }


    @Name("Obtener addressId")
    private String obtener_Id_alert() {

        String token = obtener_Token();
        System.out.println("Token: " + token);


        System.out.println("Token de funcion: " + access_token);
        System.out.println("uuid de funcion: " + uuid);

        RestAssured.baseURI = String.format("https://%s/alerts/v1/private/account/%s/alert", url_base, uuid);

        String body_request = "{\"ad_listing_service_filters\":{\"category\":\"2020\"," +
                "\"category_lv0\":\"2000\"," +
                "\"category_lv1\":\"2020\",\"lim\":\"28\"," +
                "\"municipality_multi\":\"47\"," +
                "\"price\":\"-60000\",\"region\":\"4\"}}";

        Response response = given()
                .log().all()
                .header("Accept", "application/json, text/plain, */*")
                .header("Content-Type", "application/json")
                .auth().preemptive().basic(uuid, access_token)
                .body((body_request))
                .post();


        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response);
        JsonPath jsonResponse = response.jsonPath();
        String idAlert = jsonResponse.get("data.alert.id");
        System.out.println("Address_ID en función: " + idAlert);
        id_alert = idAlert;

        return id_alert;

    }






    @Test
    @Order(1)
    @DisplayName("Test case: Obtener categorias")
    @Severity(SeverityLevel.BLOCKER)
    public void get_obtenerCategorias_200(){
        RestAssured.baseURI=String.format("https://%s/nga/api/v1.1/public/categories/filter?lang=es",url_base);

        Response response = given()
                .log().all()
                .queryParam("lang","es")
                .filter(new AllureRestAssured())
                .get();

        //Imprimir el body response
        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("id"));
        assertTrue(body_response.contains("categories"));
        assertTrue(body_response.contains("all_label"));

        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 3000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();
        //System.out.println("Las cabeceras: " + headers_response);
        assertTrue(headers_response.contains("application/json"));

    }

    @Test
    @Order(2)
    @DisplayName("Test case: Crear usuario")
    @Severity(SeverityLevel.BLOCKER)
    public void post_crearUsuario_401(){

        //Crear Usuario
        String new_user = "agente_ventas" + (Math.floor(Math.random()*987)) + "@mailinator.com";
        //String password = "12345";
        String bodyRequest = "{\"account\":{\"email\":\""+new_user+"\"}}";

        RestAssured.baseURI=String.format("https://%s/nga/api/v1.1/private/accounts?lang=es",url_base);

        Response response = given()
                .log().all()
                .queryParam("lang","es")
                .contentType("application/json")
                .auth().preemptive().basic(new_user,password)
                .body(bodyRequest)
                .post();

        //Imprimir el body response
        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(401,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("error"));
        assertTrue(body_response.contains("code"));
        assertTrue(body_response.contains("ACCOUNT_VERIFICATION_REQUIRED"));

        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();
        //System.out.println("Las cabeceras: " + headers_response);
        assertTrue(headers_response.contains("application/json"));
    }

    @Test
    @Order(3)
    @DisplayName("Test case: Obtener Usuario")
    @Severity(SeverityLevel.BLOCKER)
    public void post_ObtenerUsuario_200(){
        //Configurar
        String bodyRequest = "{\"account\":{\"email\":\""+email+"\"}}";

        //ejecucion
        RestAssured.baseURI=String.format("https://%s/nga/api/v1.1/private/accounts",url_base);

        Response response = given()
                .log().all()
                .queryParam("lang","es")
                .contentType("application/json")
                .auth().preemptive().basic(email,password)
                .body(bodyRequest)
                .post();

        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("access_token"));
        assertTrue(body_response.contains("account_id"));
        assertTrue(body_response.contains("test2022_agente@mailinator.com"));
        assertTrue(body_response.contains(email));

        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 3000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();

        assertTrue(headers_response.contains("application/json"));

    }

    @Test
    @Order(4)
    @DisplayName("Test case: Crear dirección")
    @Severity(SeverityLevel.BLOCKER)
    public void post_CrearUnaDireccion_201(){

        String token = obtener_Token();
        System.out.println("Token: " + token);

        System.out.println("Token de funcion: " + access_token);
        System.out.println("account id de funcion: " + account_id);
        System.out.println("uuid de funcion: " + uuid);


        //ejecucion
        RestAssured.baseURI=String.format("https://%s/addresses/v1/create",url_base);

        Response response = given()
                .log().all()
                .formParam("contact","Agente de ventas")
                .formParam("phone","8776655443")
                .formParam("rfc","CAPL800101")
                .formParam("zipCode","45999")
                .formParam("exteriorInfo","Miguel Hidalgo 4232")
                .formParam("interiorInfo","2")
                .formParam("region","11")
                .formParam("municipality","300")
                .formParam("area","8094")
                .formParam("alias","La oficina")
                .header("Content-type","application/x-www-form-urlencoded")
                .header("Accept","application/json, text/plain, */*")
                .auth().preemptive().basic(uuid,access_token)
                .post();


        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(201,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("addressID"));

        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();

        assertTrue(headers_response.contains("application/json"));


    }

    @Test
    @Order(5)
    @DisplayName("Test case: Crear anuncio")
    @Severity(SeverityLevel.BLOCKER)
    public void post_Crear_ununcio_200() {

        String token = obtener_Token();
        System.out.println("Token: " + token);

        System.out.println("Token de funcion: " + access_token);
        System.out.println("account id de funcion: " + account_id);
        System.out.println("uuid de funcion: " + uuid);
        System.out.println("ad_id en funcion: " + ad_id);

        String body_request = "{\"category\":\"8143\"," +
                "\"subject\":\"dog one two \"," +
                "\"body\":\"dog one two, dog one two, dog one two \"," +
                "\"region\":\"4\"," +
                "\"municipality\":\"47\"," +
                "\"area\":\"35931\"," +
                "\"price\":\"1000\"," +
                "\"phone_hidden\":\"false\"," +
                "\"show_phone\":\"true\"," +
                "\"contact_phone\":\"5566778899\"}";

        //URL
        RestAssured.baseURI=String.format("https://%s/v2/accounts/%s/up",url_base,uuid);

        Response response = given()
                .log().all()
                .header("Content-type","application/json")
                .header("Accept","*/*")
                .header("x-source","PHOENIX_DESKTOP")
                .auth().preemptive().basic(uuid,access_token)
                .body((body_request))
                .post();

        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("data"));

        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 4000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();

        assertTrue(headers_response.contains("application/json"));


    }

    @Test
    @Order(6)
    @DisplayName("Test case: Editar usuario")
    @Severity(SeverityLevel.BLOCKER)
    public void patch_editar_usuario_200() {

        int telefono = (int) (Math.floor(999999999) + 10000);




        String token = obtener_Token();
        System.out.println("Token: " + token);

        System.out.println("Token de funcion: " + access_token);
        System.out.println("account id de funcion: " + account_id);
        System.out.println("uuid de funcion: " + uuid);

        String body_request = "{\"account\":{\"name\":\"Ana\"," +
                "\"phone\":\""+telefono+"\"," +
                "\"locations\":[{\"code\":\"4\",\"key\":\"region\"," +
                "\"label\":\"Baja California\"," +
                "\"locations\":[{\"code\":\"47\"," +
                "\"key\":\"municipality\"," +
                "\"label\":\"Mexicali\"}]}]," +
                "\"professional\":false,\"phone_hidden\":true}}";

        //URL
        RestAssured.baseURI=String.format("https://%s/nga/api/v1/%s?lang=es",url_base,account_id);

        Response response = given()
                .log().all()
                .header("Content-type","application/json")
                .header("Accept","application/json, text/plain, */*")
                .header("x-source","PHOENIX_DESKTOP")
                .header("authorization", "tag:scmcoord.com,2013:api " + access_token )
                .body((body_request))
                .patch();

        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("account"));
        assertTrue(body_response.contains("name"));
        assertTrue(body_response.contains("phone"));
        assertTrue(body_response.contains("code"));


        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 4000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();

        assertTrue(headers_response.contains("application/json"));


    }

    @Test
    @Order(7)
    @DisplayName("Test case: Editar anuncio")
    @Severity(SeverityLevel.BLOCKER)
    public void put_editar_ununcio_200() {

        String ad = obtener_id();
        System.out.println("ad_id: " + ad);

        String token = obtener_Token();
        System.out.println("Token: " + token);

        System.out.println("Token de funcion: " + access_token);
        System.out.println("account id de funcion: " + account_id);
        System.out.println("uuid de funcion: " + uuid);
        System.out.println("ad_id en función: " + ad_id);

        String body_request = "{\"category\":\"8143\"," +
                "\"subject\":\"otro perro\"," +
                "\"body\":\"dog one two, dog one two, dog one two \"," +
                "\"region\":\"4\"," +
                "\"municipality\":\"47\"," +
                "\"area\":\"35931\"," +
                "\"price\":\"1000\"," +
                "\"phone_hidden\":\"false\"," +
                "\"show_phone\":\"true\"," +
                "\"contact_phone\":\"5566778899\"}";

        //URL
        RestAssured.baseURI=String.format("https://%s/v2/accounts/%s/up/%s",url_base,uuid,ad_id);

        Response response = given()
                .log().all()
                .header("Content-type","application/json")
                .header("Accept","*/*")
                .header("x-source","PHOENIX_DESKTOP")
                .auth().preemptive().basic(uuid,access_token)
                .body((body_request))
                .put();

        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("data"));
        assertTrue(body_response.contains("ad"));
        assertTrue(body_response.contains("ad_id"));
        assertTrue(body_response.contains("subject"));
        assertTrue(body_response.contains("category"));



        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 4000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();

        assertTrue(headers_response.contains("application/json"));


    }



    @Test
    @Order(8)
    @DisplayName("Test case: Editar dirección")
    @Severity(SeverityLevel.BLOCKER)
    public void patch_editar_Direccion_200(){

        String token = obtener_Token();
        System.out.println("Token: " + token);

        System.out.println("Token de funcion: " + access_token);
        System.out.println("account id de funcion: " + account_id);

        String body_request = "{\"account\":{\"phone\":\"1000001111\"," +
                "\"professional\":false,\"name\":\"Jackie\"," +
                "\"phone_hidden\":true,\"rfc\":\"MATA900901LB7\"}}";


        //ejecucion
        RestAssured.baseURI=String.format("https://%s/nga/api/v1/%s?lang=es",url_base,account_id);



        Response response = given()
                .log().all()
                .header("Content-type","application/json")
                .header("Accept","application/json, text/plain, */*")
                .header("authorization", "tag:scmcoord.com,2013:api " + access_token )
                .body((body_request))
                .patch();


        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("account"));
        assertTrue(body_response.contains("account_id"));
        assertTrue(body_response.contains("can_publish"));
        assertTrue(body_response.contains("email"));
        assertTrue(body_response.contains("email_verified"));

        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();

        assertTrue(headers_response.contains("application/json"));


    }

    @Test
    @Order(9)
    @DisplayName("Test case: Eliminar dirección")
    @Severity(SeverityLevel.BLOCKER)
    public void delete_eliminar_Direccion_200(){

        String token = obtener_Token();
        System.out.println("Token: " + token);

        String adress = obtener_addressID();
        System.out.println("address_id: " + adress);

        System.out.println("Token de funcion: " + access_token);
        System.out.println("account id de funcion: " + account_id);
        System.out.println("uuid de funcion: " + uuid);
        System.out.println("Address_ID en función: " + address_id);

        RestAssured.baseURI = String.format("https://%s/addresses/v1/delete/%s", url_base,address_id);

        Response response = given()
                .log().all()
                .header("Accept","application/json, text/plain, */*")
                .auth().preemptive().basic(uuid,access_token)
                .delete();


        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("message"));

        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();

        assertTrue(headers_response.contains("application/json"));


    }
    @Test
    @Order(10)
    @DisplayName("Test case: Crear alerta")
    @Severity(SeverityLevel.BLOCKER)
    public void post_crear_Alerta_200(){

        String token = obtener_Token();
        System.out.println("Token: " + token);


        System.out.println("Token de funcion: " + access_token);
        System.out.println("uuid de funcion: " + uuid);

        RestAssured.baseURI = String.format("https://%s/alerts/v1/private/account/%s/alert", url_base,uuid);

        String body_request = "{\"ad_listing_service_filters\":{\"category\":\"2020\"," +
                "\"category_lv0\":\"2000\"," +
                "\"category_lv1\":\"2020\",\"lim\":\"28\"," +
                "\"municipality_multi\":\"47\"," +
                "\"price\":\"-60000\",\"region\":\"4\"}}";

        Response response = given()
                .log().all()
                .header("Accept","application/json, text/plain, */*")
                .header("Content-Type", "application/json")
                .auth().preemptive().basic(uuid,access_token)
                .body((body_request))
                .post();


        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("data"));
        assertTrue(body_response.contains("alert"));
        assertTrue(body_response.contains("id"));
        assertTrue(body_response.contains("title"));
        assertTrue(body_response.contains("description"));
        assertTrue(body_response.contains("url_path"));
        assertTrue(body_response.contains("created_at"));
        assertTrue(body_response.contains("filters"));

        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();

        assertTrue(headers_response.contains("application/json"));

    }

    @Test
    @Order(11)
    @DisplayName("Test case: Eliminar alerta")
    @Severity(SeverityLevel.BLOCKER)
    public void delete_eliminar_alerta_200(){

        String token = obtener_Token();
        System.out.println("Token: " + token);
        String idAlert = obtener_Id_alert();
        System.out.println("id_alert: " + idAlert);


        System.out.println("Token de funcion: " + access_token);
        System.out.println("uuid de funcion: " + uuid);
        System.out.println("id_alert de funcion: " + id_alert);

        RestAssured.baseURI = String.format("https://%s/alerts/v1/private/account/%s/alert/%s", url_base,uuid,id_alert);


        Response response = given()
                .log().all()
                .header("Accept","application/json, text/plain, */*")
                .auth().preemptive().basic(uuid,access_token)
                .delete();


        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("data"));
        assertTrue(body_response.contains("status"));

        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();

        assertTrue(headers_response.contains("application/json"));

    }

    @Test
    @Order(12)
    @DisplayName("Test case: Consultar alerta")
    @Severity(SeverityLevel.BLOCKER)
    public void get_consultar_alerta_200() {
        String token = obtener_Token();
        System.out.println("Token: " + token);


        System.out.println("Token de funcion: " + access_token);
        System.out.println("uuid de funcion: " + uuid);

        RestAssured.baseURI = String.format("https://%s/credits/v1%s", url_base,account_id);


        Response response = given()
                .log().all()
                .header("Accept","application/json, text/plain, */*")
                .header("Content-Type","application/json")
                .auth().preemptive().basic(uuid,access_token)
                .get();


        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("balance"));
        assertTrue(body_response.contains("Transactions"));

        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();

        assertTrue(headers_response.contains("application/json"));

    }

    @Test
    @Order(13)
    @DisplayName("Test case: Guardar favoritos")
    @Severity(SeverityLevel.BLOCKER)
    public void post_guardar_favoritos_200() {
        String token = obtener_Token();
        System.out.println("Token: " + token);


        System.out.println("Token de funcion: " + access_token);
        System.out.println("uuid de funcion: " + uuid);

        RestAssured.baseURI = String.format("https://%s/favorites/v1/private/accounts/%s", url_base,uuid);

        String body_request = "{\"list_ids\":[938946242]}";



        Response response = given()
                .log().all()
                .header("Accept","application/json, text/plain, */*")
                .header("Content-Type","application/json")
                .auth().preemptive().basic(uuid,access_token)
                .body(body_request)
                .post();


        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("data"));
        assertTrue(body_response.contains("added"));

        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();

        assertTrue(headers_response.contains("application/json"));

    }

    @Test
    @Order(14)
    @DisplayName("Test case: Quitar favoritos")
    @Severity(SeverityLevel.BLOCKER)
    public void delete_quitar_favoritos_200() {
        String token = obtener_Token();
        System.out.println("Token: " + token);


        System.out.println("Token de funcion: " + access_token);
        System.out.println("uuid de funcion: " + uuid);

        RestAssured.baseURI = String.format("https://%s/favorites/v1/private/accounts/%s", url_base,uuid);


        String body_request = "{\"list_ids\":[938946242]}";



        Response response = given()
                .log().all()
                .header("Accept","application/json, text/plain, */*")
                .header("Content-Type","application/json")
                .auth().preemptive().basic(uuid,access_token)
                .body(body_request)
                .delete();


        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("data"));
        assertTrue(body_response.contains("deleted"));

        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();

        assertTrue(headers_response.contains("application/json"));

    }

    @Test
    @Order(15)
    @DisplayName("Test case: Recuperar contraseña")
    @Severity(SeverityLevel.BLOCKER)
    public void post_recuperar_contraseña_200() {
        String token = obtener_Token();
        System.out.println("Token: " + token);


        System.out.println("Token de funcion: " + access_token);
        System.out.println("uuid de funcion: " + uuid);

        RestAssured.baseURI = String.format("https://%s/nga/api/v1/private/accounts/otp?lang=es", url_base);

        String body_request = "{\"account\":{\"email\":\"qa2022@mailinator.com\"}}";



        Response response = given()
                .log().all()
                .header("Accept","application/json, text/plain, */*")
                .header("Content-Type","application/json")
                .body(body_request)
                .post();


        String body_response = response.getBody().asString();
        System.out.println("Body response: " + body_response );

        //Pruebas
        //al menos 5 pruebas

        //Validar el status response
        assertEquals(200,response.getStatusCode());

        //Validar que nuestro body no este vacio
        assertNotNull(body_response);

        //Validar que el body contenga la palabra ID
        assertTrue(body_response.contains("account"));
        assertTrue(body_response.contains("account_id"));
        assertTrue(body_response.contains("email"));

        //Validar el tiempo de respuesta
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        //Validar los headers
        String headers_response = response.getHeaders().toString();

        assertTrue(headers_response.contains("application/json"));

    }


}
