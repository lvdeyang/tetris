package com.sumavision.tetris.sdk.constraint.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Configuration
public class ConstraintConfig {

	@Bean
	public SpelExpressionParser spelExpressionParser(){
		return new SpelExpressionParser();
	}
	
}
