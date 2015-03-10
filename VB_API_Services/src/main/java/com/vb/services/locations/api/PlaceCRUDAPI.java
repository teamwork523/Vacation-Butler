package com.vb.services.locations.api;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(value = MediaType.APPLICATION_JSON)
public class PlaceCRUDAPI {
	// TODO: the actual implementation
	@GET
	@Timed
    public String blogsTimeline() {
		return "Successful!";
	}
}
