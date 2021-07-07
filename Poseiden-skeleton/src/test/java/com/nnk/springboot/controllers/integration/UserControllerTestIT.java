package com.nnk.springboot.controllers.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;

/**
 * Integration test class for UserController.
 * <p>This test uses a test database that is defined in <b>\src\test\resources\application.properties</b></p>
 * 
 * @author jerome
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTestIT {

	Logger logger = LoggerFactory.getLogger(TradeControllerIT.class);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private UserService userService;

	final String password = "password_$A1";
	final String password_encoded = "password_$A1_encoded";

	User user1;

	//When using @SpringBootTest annotation to test controllers with Spring Security, it's necessary to explicitly
	//configure the filter chain when setting up MockMvc:
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();

		//id needs to be null to create a new User in db, otherwise try to update id and fail => no creation
		user1 = new User(null, "username1", "password1", "fullname1", "USER");

	}

	@Test
	void GET_userList_shouldSucceedWith200() throws Exception {
		mockMvc.perform(get("/user/list"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("user/list"))
		;
	}

	@Transactional // rollback will be done automatically
	@Test
	void POST_userValidate_shouldSucceedWithRedirection() throws Exception {

		//ACT+ASSERT:
		mockMvc.perform(post("/user/validate")
				.param("username", "username_999")
				.param("password", password)
				.param("fullname", "fullname_999")
				.param("role", "USER")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/user/list"))
		;

		List<User> listUser = userService.findAll();
		assertEquals(1,listUser.size()); //since we are in an empty test database 
		assertEquals("username_999",listUser.get(0).getUsername());
		//Cannot check password since it's encoded
		assertEquals("fullname_999",listUser.get(0).getFullname());
		assertEquals("USER",listUser.get(0).getRole());
	}

	@Transactional // rollback will be done automatically
	@Test
	void POST_userUpdate_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		//insert a trade to update in the database:
		userService.save(user1);
		//With a @GeneratedValue type id you can't know that value in advance (before actually writing it).
		//However once you persist your Bean, the id field will be populated in that bean instance and you can obtain
		//it without needing to do an extra query for it : 
		int id = user1.getId();

		//ACT+ASSERT:
		mockMvc.perform(post("/user/update/"+id)
				.param("username", "username_999")
				.param("password", password)
				.param("fullname", "fullname_999")
				.param("role", "USER")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/user/list"))
		;

		Optional<User> resultUser = userService.findById(id);
		assertTrue(resultUser.isPresent());
		assertEquals("username_999",resultUser.get().getUsername());
		//Cannot check password since it's encoded
		assertEquals("fullname_999",resultUser.get().getFullname());
		assertEquals("USER",resultUser.get().getRole());
	}

	@Transactional // rollback will be done automatically
	@Test
	void POST_userDelete_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		//insert a trade to delete in the database: 
		userService.save(user1);
		int id = user1.getId();

		//ACT+ASSERT:
		mockMvc.perform(get("/user/delete/"+id)
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/user/list"))
		;

		Optional<User> resultUser = userService.findById(id);
		assertFalse(resultUser.isPresent());
	}


}
