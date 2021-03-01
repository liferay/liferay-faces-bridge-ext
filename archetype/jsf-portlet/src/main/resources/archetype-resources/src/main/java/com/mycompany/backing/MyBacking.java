package com.mycompany.backing;

import com.mycompany.dto.Greeting;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named
@RequestScoped
// CDI will only be activated if WEB-INF/beans.xml is present. If building with
// Maven, use the "-P cdi" profile to generate the WEB-INF/beans.xml descriptor.
public class MyBacking {

	private Greeting greeting = new Greeting();

	public Greeting getGreeting() {
		return greeting;
	}
}