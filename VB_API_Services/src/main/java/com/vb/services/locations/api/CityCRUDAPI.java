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

import com.vb.services.model.CreateCityRq;
import com.vb.services.model.UpdateCityRq;

@Path("/api/city")
@Produces(value = MediaType.APPLICATION_JSON)
public interface ICityCRUDAPI {
	
	@POST
	@Path("/createcity")
    public Response createCity(CreateCityRq city);
	
	@GET
	@Path("/readcitybyname/{city_name}")
    public Response readCityByCityName(@PathParam("city_name") String cityName);
	
	@PUT
	@Path("/updatecity/cityname/{city_name}/city_id/{city_id}")
    public Response updateCityByNameAndID(@PathParam("city_id") Integer cityID, UpdateCityRq city);
	
	@DELETE
	@Path("/deletecity/cityname/{city_name}/city_id/{city_id}")
    public Response deleteCityByNameAndID(@PathParam("city_id") Integer cityID);
}
