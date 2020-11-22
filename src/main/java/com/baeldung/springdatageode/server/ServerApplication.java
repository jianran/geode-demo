package com.baeldung.springdatageode.server;

import com.baeldung.springdatageode.domain.Author;
import com.baeldung.springdatageode.server.fn.AverageAgeFunctionImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableLocator;

/**
	* @author <a href="mailto:josh@joshlong.com">Josh Long</a>
	*/
@SpringBootApplication
@ComponentScan(basePackageClasses = {
	ServerApplication.class, AverageAgeFunctionImpl.class})
@CacheServerApplication(locators = "localhost[10334]")
@EnableLocator
public class ServerApplication {

	public static void main(String args[]) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
