package com.CareerPathway.CareerPathway;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Disabled("This test requires a database and is disabled for CI/CD")
class CareerPathwayApplicationTests {

	@Test
	void contextLoads() {
	}

}
