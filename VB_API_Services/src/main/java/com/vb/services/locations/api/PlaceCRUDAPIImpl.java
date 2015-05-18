package com.vb.services.locations.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vb.dynamodb.domain.PlaceDomainServiceImpl;
import com.vb.dynamodb.model.PlaceItem;
import com.vb.services.logging.VBLogger;
import com.vb.services.model.CreatePlaceRs;
import com.vb.services.model.CreatePlaceRq;

public class PlaceCRUDAPIImpl implements PlaceCRUDAPI {
	
	private static final VBLogger LOGGER = VBLogger.getLogger(PlaceCRUDAPIImpl.class);
	
	// TODO: Use Spring Injection here
	protected PlaceDomainServiceImpl placeDomainService = new PlaceDomainServiceImpl();
	
	//////////////////////////////////////
	//////// API implementation //////////
	//////////////////////////////////////
	@Override
	public Response createPlace(CreatePlaceRq place) {
		LOGGER.info("Calling Create Place API");
		try {
			PlaceItem newPlace = new PlaceItem(place);
			newPlace = placeDomainService.addPlace(newPlace);
			CreatePlaceRs cpRs = new CreatePlaceRs(newPlace.getPlaceID());
			return Response.ok(cpRs).build();
		} catch (Exception e) {
			CreatePlaceRs cpRs = new CreatePlaceRs(e);
			Status st = LocationServiceResultMapper.httpStatus(e);
			return Response.status(st).entity(cpRs).build();
		}
	}

	@Override
	public Response readPlaceByCityNameAndID(String cityName, Integer cityID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response readPlaceByPlaceID(Long placeID) {
		// TODO Auto-generated method stub
		return null;
	}

}
