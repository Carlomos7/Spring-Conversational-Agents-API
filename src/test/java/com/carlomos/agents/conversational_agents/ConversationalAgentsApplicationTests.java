package com.carlomos.agents.conversational_agents;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = DatabaseTestConfig.Initializer.class)
class ConversationalAgentsApplicationTests {

	@Test
	void contextLoads() {
	}

}
