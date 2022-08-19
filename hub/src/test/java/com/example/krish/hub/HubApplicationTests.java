package com.example.krish.hub;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import com.example.krish.hub.entity.Hub;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;

@ActiveProfiles(profiles = "dev")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = com.example.krish.hub.HubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HubApplicationTests {

	@Test
	void contextLoads() {
	}

	@Value("${hub.path:/v1/hub}")
	private String uri;

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() throws Exception {
		RestAssured.port = port;

		RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
				new ObjectMapperConfig().jackson2ObjectMapperFactory(new Jackson2ObjectMapperFactory() {
					@Override
					public ObjectMapper create(Type arg0, String arg1) {
						return objectMapper;
					}
				}));
	}

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	public void initialiseRestAssuredMockMvcWebApplicationContext() {
		RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
	}

	@Test
	@Order(1)
	public void testCreateAndGet() throws Exception {
		Hub hub = new Hub();
		hub.setUser("Krish");
		hub.setAge("31");
		hub.setRole("admin");

		HashMap<String,Object >response = io.restassured.RestAssured.with().body(hub).when().contentType(ContentType.JSON)
				.request("POST", uri).then().statusCode(201).extract().path("data");
		
		String checkHref = uri + "/" + response.get("id").toString();
		assertTrue(response.get("href").equals(checkHref));
		
		io.restassured.RestAssured.given().when().get(uri+"/" +response.get("id").toString()).then().log().ifValidationFails().statusCode(HttpStatus.OK.value());
		
		
	}
}
