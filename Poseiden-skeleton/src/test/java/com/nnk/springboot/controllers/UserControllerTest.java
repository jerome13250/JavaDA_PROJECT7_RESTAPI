package com.nnk.springboot.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.dto.UserFormDTO;
import com.nnk.springboot.services.UserService;
import com.nnk.springboot.testconfig.SpringWebTestConfig;

//@WebMvcTest tells Spring Boot to instantiate only the web layer and not the entire context
@WebMvcTest(controllers = UserController.class) 
//Need to create a UserDetailsService in SpringSecurityWebTestConfig.class because @Service are not loaded by @WebMvcTest :
@Import(SpringWebTestConfig.class)

class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserService userService;
	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@MockBean
	private ModelMapper modelMapper;
	
	final String password = "password_$A1";
	final String password_encoded = "password_$A1_encoded";
	
	User user1;
	List<User> userList;
	
	@BeforeEach
	void initialize() {
		new User();
		user1 = new User(1, "username1", "password1", "fullname1", "USER");
		User user2 = new User(2, "username2", "password2", "fullname2", "USER");
		User user3 = new User(3, "username3", "password3", "fullname3", "USER");
		userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);
		userList.add(user3);		
		
	}
	
	@Test
	void GET_userList_shouldSucceedWith200() throws Exception {
		mockMvc.perform(get("/user/list"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("user/list"))
		;
	}
	
	@Test
	void GET_userAdd_shouldSucceedWith200() throws Exception {
		mockMvc.perform(get("/user/add"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("user/add"))
		;
	}
	
	@Test
	void POST_userValidate_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		when(userService.existsByUsername("username_999")).thenReturn(Boolean.FALSE);
		when(bCryptPasswordEncoder.encode(password)).thenReturn(password_encoded);
		User user = new User(999, "username_999", password, "fullname_999", "USER");
		user.setId(null);
		when(modelMapper.map(any(UserFormDTO.class), eq(User.class))).thenReturn(user);
		
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
		
		//No direct access to userFormDTO so need ArgumentCaptor
		ArgumentCaptor<UserFormDTO> userFormDTOCaptor = ArgumentCaptor.forClass(UserFormDTO.class);
		verify(modelMapper).map(userFormDTOCaptor.capture(), eq(User.class));
		UserFormDTO mappedUserFormDTO = userFormDTOCaptor.getValue();
		assertEquals("username_999",mappedUserFormDTO.getUsername());
		assertEquals(password,mappedUserFormDTO.getPassword());
		assertEquals("fullname_999",mappedUserFormDTO.getFullname());
		assertEquals("USER",mappedUserFormDTO.getRole());
		
		//direct access to user since it is provided in ARRANGE 
		verify(userService).save(user);
		assertNull(user.getId());
		assertEquals("username_999",user.getUsername());
		assertEquals(password_encoded,user.getPassword());
		assertEquals("fullname_999",user.getFullname());
		assertEquals("USER",user.getRole());
	}
	
	@Test
	void POST_userValidate_formValidationFailed_NoData() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/user/validate")
				//.param("username", "username_999")
				//.param("password", password)
				//.param("fullname", "fullname_999")
				//.param("role", "USER")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("user/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("userFormDTO", 4))
		.andExpect(model().attributeHasFieldErrorCode("userFormDTO", "username", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("userFormDTO", "password", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("userFormDTO", "fullname", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("userFormDTO", "role", "NotBlank"))
		;
		
		//User must not be saved
		verify(userService,never()).save(any(User.class));
		
	}
	
	@Test
	void POST_userValidate_formValidationFailed_InvalidRole() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/user/validate")
				.param("username", "username_999")
				.param("password", password)
				.param("fullname", "fullname_999")
				.param("role", "INVALID_ROLE")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("user/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("userFormDTO", 1))
		.andExpect(model().attributeHasFieldErrorCode("userFormDTO", "role", "Pattern"))
		;
		
		//User must not be saved
		verify(userService,never()).save(any(User.class));
	}
	
	@Test
	void POST_userValidate_formValidationFailed_InvalidPassword() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/user/validate")
				.param("username", "username_999")
				.param("password", "!!!!!!!!!!!") // "!" is not in our list of special characters 
				.param("fullname", "fullname_999")
				.param("role", "USER")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("user/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("userFormDTO", 4))
		.andExpect(model().attributeHasFieldErrorCode("userFormDTO", "password", "Pattern"))
		;
		
		//User must not be saved
		verify(userService,never()).save(any(User.class));
	}
	
	@Test
	void POST_userValidate_usernameAlreadyExists() throws Exception {
		//ARRANGE:
		when(userService.existsByUsername("username_999")).thenReturn(Boolean.TRUE);
				
		//ACT+ASSERT:
		mockMvc.perform(post("/user/validate")
				.param("username", "username_999")
				.param("password", password)
				.param("fullname", "fullname_999")
				.param("role", "USER")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("user/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("userFormDTO", 1))
		.andExpect(model().attributeHasFieldErrorCode("userFormDTO", "username", ""))
		;
		
		//User must not be saved
		verify(userService,never()).save(any(User.class));
	}
	
	
	@Test
	void GET_showUpdateForm_shouldSucceedWith200() throws Exception {
		//ARRANGE:
		when(userService.findById(1)).thenReturn(Optional.of(user1));
		UserFormDTO userFormDTO = new UserFormDTO(1, "username1", "password1", "fullname1", "USER");
		when(modelMapper.map(any(User.class), eq(UserFormDTO.class))).thenReturn(userFormDTO);
		
		//ACT+ASSERT:
		mockMvc.perform(get("/user/update/1"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("user/update"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("userFormDTO"))
		;
	}
	
	@Test
	void GET_showUpdateForm_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(userService.findById(1)).thenReturn(Optional.empty());
		
		//ACT+ASSERT:
		mockMvc.perform(get("/user/update/1"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("errorMsg"))
		;
	}
	
	@Test
	void POST_userUpdate_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		when(userService.existsById(1)).thenReturn(Boolean.TRUE);
		when(bCryptPasswordEncoder.encode(password)).thenReturn(password_encoded);
		User user = new User(1, "username_999", password, "fullname_999", "USER");
		when(modelMapper.map(any(UserFormDTO.class), eq(User.class))).thenReturn(user);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/user/update/1")
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
		
		//No direct access to userFormDTO so need ArgumentCaptor
		ArgumentCaptor<UserFormDTO> userFormDTOCaptor = ArgumentCaptor.forClass(UserFormDTO.class);
		verify(modelMapper).map(userFormDTOCaptor.capture(), eq(User.class));
		UserFormDTO mappedUserFormDTO = userFormDTOCaptor.getValue();
		assertEquals("username_999",mappedUserFormDTO.getUsername());
		assertEquals(password,mappedUserFormDTO.getPassword());
		assertEquals("fullname_999",mappedUserFormDTO.getFullname());
		assertEquals("USER",mappedUserFormDTO.getRole());
		
		//direct access to user since it is provided in ARRANGE 
		verify(userService).save(user);
		assertEquals(1,user.getId());
		assertEquals("username_999",user.getUsername());
		assertEquals(password_encoded,user.getPassword());
		assertEquals("fullname_999",user.getFullname());
		assertEquals("USER",user.getRole());
	}
	
	@Test
	void POST_userUpdate_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(userService.existsById(1)).thenReturn(Boolean.FALSE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/user/update/1")
				.param("username", "username_999")
				.param("password", password)
				.param("fullname", "fullname_999")
				.param("role", "USER")
				.with(csrf())
				)
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().attributeExists("errorMsg"))
		;
		
		//User must not be saved
		verify(userService,never()).save(any(User.class));
	}
	
	@Test
	void POST_userUpdate_FormValidationFail_NoDAta_shouldReturnToUpdatePage() throws Exception {
		//ARRANGE:
		when(userService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/user/update/1")
				//.param("username", "username_999")
				//.param("password", password)
				//.param("fullname", "fullname_999")
				//.param("role", "USER")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("user/update"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("userFormDTO", 4))
		.andExpect(model().attributeHasFieldErrorCode("userFormDTO", "username", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("userFormDTO", "password", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("userFormDTO", "fullname", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("userFormDTO", "role", "NotBlank"))
		;
		
		//User must not be saved
		verify(userService,never()).save(any(User.class));
	}
	
	@Test
	void POST_userDelete_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		when(userService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(get("/user/delete/1")
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/user/list"))
		;
		
		verify(userService).deleteById(1);
	}
	
	@Test
	void GET_userDelete_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(userService.existsById(1)).thenReturn(Boolean.FALSE);
		
		//ACT+ASSERT:
		mockMvc.perform(get("/user/delete/1")
					)
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().attributeExists("errorMsg"))
		;
		
		//User must not be deleted
		verify(userService,never()).deleteById(1);
	}
	
	
}
