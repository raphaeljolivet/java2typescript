package com.example.model;

public class Person {
	private String email;
	private String firstName;
	private String lastName;
		
	public Person() {
	}
	
	public Person( final String email ) {
		this.email = email;
	}
	
	public Person( final String firstName, final String lastName ) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail( final String email ) {
		this.email = email;
	}
		
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setFirstName( final String firstName ) {
		this.firstName = firstName;
	}
	
	public void setLastName( final String lastName ) {
		this.lastName = lastName;
	}		
}
