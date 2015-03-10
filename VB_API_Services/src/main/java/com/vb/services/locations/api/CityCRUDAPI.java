package com.vb.services.locations.api;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.vb.services.model.CityRq;

@Path("/api/city")
@Produces(value = MediaType.APPLICATION_JSON)
public interface CityCRUDAPI {
	
	@POST
	@Path("/createcity/{city_id}")
    public Response createCity(CityRq city);
	
	@GET
	@Path("/readcitybyid/{city_id}")
    public Response readCityByID(@PathParam("city_id") String cityID);
	
	@GET
	@Path("/readcitybyname/{city_name}")
    public Response readCityByCityName(@PathParam("city_name") String cityName);
	
	@PUT
	@Path("/updatecity/{city_id}")
    public Response updateCity(@PathParam("city_id") String id, CityRq city);
	
	@DELETE
	@Path("/deletecity/{city_id}")
    public Response deleteCity(@PathParam("city_id") String id, CityRq city);
}
