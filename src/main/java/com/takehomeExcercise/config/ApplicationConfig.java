package com.takehomeExcercise.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.takehomeExcercise.api.CoinBaseBuySaleAPI;

@ApplicationPath("rest")
public class ApplicationConfig extends Application {
	private Set<Object> singletons = new HashSet<Object>();

	public ApplicationConfig() {
		singletons.add(new CoinBaseBuySaleAPI());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}