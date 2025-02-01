package com.example.test;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TestApplicationTests {

	@InjectMocks
	TestController testController;

	@Test
	public void testGetSpeakerSimDetails_MissingMerchantIdParameters() {
		// Call controller method without required parameters
		ResponseEntity<String> response = (ResponseEntity<String>) testController.testMethod("123");
		// Verify response
		assertEquals("Test123", response.getBody()); // Assuming 400 is Bad Request status
	}

}
