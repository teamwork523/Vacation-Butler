package com.vb.services.locations.api;

import com.codahale.metrics.annotation.Timed;
import com.vb.services.model.CreatePlaceRq;
import com.vb.services.model.ReadPlacesByKeywordRq;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/place")
@Produces(value = MediaType.APPLICATION_JSON)
public interface PlaceCRUDAPI {
	
	@POST
	@Path("/createplace")
	@Timed
    public Response createPlace(CreatePlaceRq place);
	
	@GET
	@Path("/readplaces/cityname/{city_name}/cityid/{city_id}")
	@Timed
	public Response readPlaceByCityNameAndID(@PathParam("city_name") String cityName, 
											 @PathParam("city_id") Integer cityID);
	
	@GET
	@Path("/readplacebyplaceid/{place_id}")
	@Timed
	public Response readPlaceByPlaceID(@PathParam("place_id") Long placeID);
	
	@POST
	@Path("/readplaces/keyword/{place_keyword}")
	@Timed
	public Response readPlacesByKeyword(@PathParam("place_keyword") String placeKeyword,
									    ReadPlacesByKeywordRq requestBody);
	
	@DELETE
	@Path("/deleteplacebyplaceid/{place_id}")
	@Timed
	public Response deletePlaceByID(@PathParam("place_id") Long placeID);
}
