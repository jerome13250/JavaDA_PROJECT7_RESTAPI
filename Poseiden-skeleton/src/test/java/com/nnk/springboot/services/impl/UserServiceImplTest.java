package com.nnk.springboot.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

/**
 * Unit test class for UserService
 * @author jerome
 *
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userServiceImpl;

	@Mock
	UserRepository userRepository;

	User user1;
	Optional<User> optuser1;
	List<User> ListOfUser;

	@BeforeEach
	void initialize() {
		User user1 = new User(null, "username1", "password1", "fullname1", "USER");
		optuser1 = Optional.of(user1);
		User user2 = new User(null, "username2", "password2", "fullname2", "USER");
		ListOfUser = new ArrayList<>();
		ListOfUser.add(user1);
		ListOfUser.add(user2);
	}

	@Test
	void test_findAll() {
		//ARRANGE:
		when(userRepository.findAll()).thenReturn(ListOfUser);

		//ACT:
		List<User> resultListOfUser = userServiceImpl.findAll();

		//ASSERT:
		assertEquals(ListOfUser, resultListOfUser);
	}

	@Test
	void test_save() {
		//ACT:
		userServiceImpl.save(user1);

		//ASSERT:
		verify(userRepository).save(user1);
	}

	@Test
	void test_findById() {
		//ARRANGE:
		when(userRepository.findById(1)).thenReturn(optuser1);

		//ACT:
		Optional<User> resultOptUser = userServiceImpl.findById(1);

		//ASSERT:
		assertEquals(optuser1, resultOptUser);
	}

	@Test
	void test_existsById() {
		//ARRANGE:
		when(userRepository.existsById(1)).thenReturn(Boolean.TRUE);

		//ACT:
		Boolean result = userServiceImpl.existsById(1);

		//ASSERT:
		assertTrue(result);
	}

	@Test
	void test_deleteById() {
		//ACT:
		userServiceImpl.deleteById(1);

		//ASSERT:
		verify(userRepository).deleteById(1);
	}

	@Test
	void test_findByUsername() {
		//ARRANGE:
		when(userRepository.findByUsername("username1")).thenReturn(user1);

		//ACT:
		User resultUser = userServiceImpl.findByUsername("username1");

		//ASSERT:
		assertEquals(user1, resultUser);
	}
	
	@Test
	void test_existsByUsername() {
		//ARRANGE:
		when(userRepository.existsByUsername("username1")).thenReturn(Boolean.TRUE);

		//ACT:
		Boolean result = userServiceImpl.existsByUsername("username1");

		//ASSERT:
		assertTrue(result);
	}

}
