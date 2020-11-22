package com.baeldung.springdatageode.server.fn;

import com.baeldung.springdatageode.domain.Author;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.data.gemfire.function.annotation.RegionData;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
	* @author <a href="mailto:josh@joshlong.com">Josh Long</a>
	*/
@Component
public class AverageAgeFunctionImpl {

	@GemfireFunction
	public Double average(@RegionData Map<String, Author> data) {
		Double avg = data
			.values()
			.stream()
			.map(Author::getAge)
			.collect(Collectors.averagingDouble(x -> x));
		return avg;
	}
}
