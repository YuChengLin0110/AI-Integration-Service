package com.example.ai.infrastructure.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.ai.infrastructure.llm.mybatis.handler.VectorTypeHandler;

@Configuration
@MapperScan(basePackages = "com.example.ai.infrastructure.mybatis.mapper")
public class MyBatisConfig {
	
	@Bean
	public org.apache.ibatis.session.Configuration mybatisConfiguration(){
		// 建立 MyBatis 的 Configuration 物件
		org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
		
		// 註冊自訂 TypeHandler，處理 PgVector 的 vector 欄位
		config.getTypeHandlerRegistry().register(VectorTypeHandler.class);
		
		// 將 config 註冊為 Spring Bean，讓 MyBatis 使用
		return config;
	}
}
