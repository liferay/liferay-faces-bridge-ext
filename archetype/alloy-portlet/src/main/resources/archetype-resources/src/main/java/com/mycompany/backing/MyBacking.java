package com.mycompany.backing;

import com.mycompany.dto.Greeting;

import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

@Named
@RequestScoped
// If building with Maven, specify the "-P cdi" profile to activate CDI.
public class MyBacking {

	private Greeting greeting = new Greeting();

	public Greeting getGreeting() {
		return greeting;
	}
}