package com.mycompany.backing;

import com.mycompany.dto.Greeting;

public class MyBacking {

	private Greeting greeting = new Greeting();

	public Greeting getGreeting() {
		return greeting;
	}
}