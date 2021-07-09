package com.nnk.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.nnk.springboot.testconfig.SpringWebIntegrationTestConfig;

@SpringBootTest
@Import(SpringWebIntegrationTestConfig.class)
class PoseidenApplicationTests {

	@Test
	void contextLoads() {
	}

}