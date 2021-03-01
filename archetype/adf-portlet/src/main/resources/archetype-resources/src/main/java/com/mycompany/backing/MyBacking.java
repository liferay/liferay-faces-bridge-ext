package com.mycompany.backing;

import com.mycompany.dto.Greeting;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named
@RequestScoped
public class MyBacking {

	private Greeting greeting = new Greeting();

	public Greeting getGreeting() {
		return greeting;
	}
}