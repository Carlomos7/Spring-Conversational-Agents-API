package com.carlomos.agents;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.carlomos.agents.DatabaseTestConfig.Initializer;

@SpringBootTest
@ContextConfiguration(initializers = DatabaseTestConfig.Initializer.class)
class ConversationalAgentsApplicationTests {

	@Test
	void contextLoads() {
	}

}
