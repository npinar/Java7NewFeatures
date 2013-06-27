package com.myjavasolutions.java7.tutorial;

import java.io.IOException;

import javax.xml.datatype.DatatypeConfigurationException;

public class Demo {

	public String getUserAccessLevel(User user) {
		
		String role = user.getRole();
		String accessLevel;
		
		switch (role) {
		case "admin":
			accessLevel = "admin";
			break;
		case "customer":
			accessLevel = "customer";
			break;
		case "user":
			accessLevel = "user";
			break;
		default:
			accessLevel = "no access";
			break;
		}

		return accessLevel;
	}
	
	public void generateIOException() throws IOException {
		throw new IOException();
	}

	public void generateParsingException() throws DatatypeConfigurationException  {
		throw new DatatypeConfigurationException();
	}
}
