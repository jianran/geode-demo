package com.baeldung.springdatageode.app;

import org.springframework.data.gemfire.function.annotation.OnRegion;

import java.util.Collection;

@OnRegion(region = "Authors")
public interface AverageAgeFunctionInvoker {

	Collection<Double> average();
}
