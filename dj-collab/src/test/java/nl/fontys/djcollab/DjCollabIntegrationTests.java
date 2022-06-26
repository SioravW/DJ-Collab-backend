package nl.fontys.djcollab;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import nl.fontys.djcollab.Domain.DTO.AddUserDTO;
import nl.fontys.djcollab.Domain.DTO.UpdateUserDTO;
import nl.fontys.djcollab.Domain.DTO.UserDTO;
import nl.fontys.djcollab.Domain.Events.UsernameChangeEvent;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.CoreMatchers.is;

import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = DjCollabIntegrationTests.Initializer.class)
@Testcontainers
class DjCollabIntegrationTests {

	@Autowired
	private MockMvc mvc;

	@Container
	public static final GenericContainer rabbit = new GenericContainer(DockerImageName.parse("rabbitmq:3.8-management"))
			.withExposedPorts(5672, 15672);

	private final ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testGetAllUser() throws Exception {
		mvc.perform(get("/user").with(oidcLogin()
				.authorities(new SimpleGrantedAuthority("ROLE_api-user"))))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void testGetAllUserAnonymous() throws Exception {
		mvc.perform(get("/user").with(oidcLogin())).andExpect(status().isForbidden());
	}

	@Test
	public void testAddUser() throws Exception {
		AddUserDTO user = new AddUserDTO();
		user.setUsername("studentje");
		user.setPassword("schoolisleuk");
		MvcResult result = mvc.perform(post("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(user))
				.characterEncoding("utf-8")
				.with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_api-user"))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andReturn();
	}

	@Test
	public void testUpdateUser() throws Exception {

		String url = "http://" + rabbit.getHost() + ":" + rabbit.getMappedPort(15672) + "/api/overview?columns=message_stats";

		// create auth credentials
		String authStr = "guest:guest";
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());

		// create headers
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);

		// create request
		HttpEntity request = new HttpEntity(headers);

		// make a request
		ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, request, String.class);

		System.out.println(response);

		// get JSON response
		String json = response.getBody();

		int initPublishNr = -1;
		int afterPublishNr = -1;
		Pattern pattern = Pattern.compile("\\\"publish\\\":(\\d+)");
		Matcher matcher = pattern.matcher(json);
		if(matcher.find()) {
			initPublishNr = Integer.parseInt(matcher.group(1));
		}
		else {
			initPublishNr = 0;
		}

		MvcResult result = mvc.perform(get("/user").with(oidcLogin()
				.authorities(new SimpleGrantedAuthority("ROLE_api-user"))))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		List<UserDTO> existingUsers = mapper.readValue(responseBody, new TypeReference<List<UserDTO>>(){});
		UpdateUserDTO user = new UpdateUserDTO();
		user.setExternalId(existingUsers.get(0).getId());
		user.setUsername("NieuweUsername");
		user.setPassword(existingUsers.get(0).getPassword());

		result = mvc.perform(put("/user/{id}", user.getExternalId().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(user))
				.with(oidcLogin()
				.authorities(new SimpleGrantedAuthority("ROLE_api-user"))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("NieuweUsername"))
				.andDo(print())
				.andReturn();

		Thread.sleep(5000);

		response = new RestTemplate().exchange(url, HttpMethod.GET, request, String.class);

		// get JSON response
		json = response.getBody();

		matcher = pattern.matcher(json);

		if(matcher.find()) {
			afterPublishNr = Integer.parseInt(matcher.group(1));
		}

		assertTrue(afterPublishNr != -1);
		assertTrue(initPublishNr != -1);
		assertEquals(initPublishNr + 1, afterPublishNr);
	}


	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			val values = TestPropertyValues.of(
					"spring.rabbitmq.host=" + rabbit.getHost(),
					"spring.rabbitmq.port=" + rabbit.getMappedPort(5672)
			);
			values.applyTo(configurableApplicationContext);
		}
	}

}
