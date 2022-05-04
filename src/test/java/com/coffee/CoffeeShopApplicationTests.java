package com.coffee;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootTest
public abstract class CoffeeShopApplicationTests {

	@Bean
	public ObjectMapper objectMapper() {
		return Mockito.mock(ObjectMapper.class);
	}

	@Bean
	public ModelMapper modelMapper() {
		return Mockito.mock(ModelMapper.class);
	}

	@Test
	void contextLoads() {
	}

}
