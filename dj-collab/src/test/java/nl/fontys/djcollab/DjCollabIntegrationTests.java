package nl.fontys.djcollab;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import nl.fontys.djcollab.API.Contollers.UserController;
import nl.fontys.djcollab.Database.Repository.UserRepository;
import nl.fontys.djcollab.Domain.DTO.AddUserDTO;
import nl.fontys.djcollab.Domain.DTO.UserDTO;
import nl.fontys.djcollab.Domain.Models.User;
import nl.fontys.djcollab.Domain.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.config.http.MatcherType.mvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class DjCollabIntegrationTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserService service;

	private final ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testGetAllUser() throws Exception {
		mvc.perform(get("/user").with(oidcLogin()
				.authorities(new SimpleGrantedAuthority("ROLE_api-user"))))
				.andExpect(status().isOk());
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
				.andReturn();
	}

}
