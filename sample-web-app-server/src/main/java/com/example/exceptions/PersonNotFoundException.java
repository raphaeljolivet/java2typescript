package com.example.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class PersonNotFoundException extends WebApplicationException {
	private static final long serialVersionUID = -2894269137259898072L;
	
	public PersonNotFoundException( final String email ) {
		super(
			Response
				.status( Status.NOT_FOUND )
				.entity( "Person not found: " + email )
				.build()
		);
	}
}
