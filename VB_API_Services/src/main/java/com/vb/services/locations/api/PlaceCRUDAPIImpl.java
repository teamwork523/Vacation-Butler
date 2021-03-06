package com.vb.services.locations.api;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vb.dynamodb.domain.PlaceDomainServiceImpl;
import com.vb.dynamodb.model.PlaceItem;
import com.vb.services.logging.VBLogger;
import com.vb.services.model.CreatePlaceRs;
import com.vb.services.model.CreatePlaceRq;
import com.vb.services.model.DeletePlaceRs;
import com.vb.services.model.ReadMultiplePlacesRs;
import com.vb.services.model.ReadPlacesByKeywordRq;

public class PlaceCRUDAPIImpl implements PlaceCRUDAPI {
	
	private static final VBLogger LOGGER = VBLogger.getLogger(PlaceCRUDAPIImpl.class);
	
	// TODO: Use Spring Injection here
	protected PlaceDomainServiceImpl placeDomainService = new PlaceDomainServiceImpl();
	
	//////////////////////////////////////
	//////// API implementation //////////
	//////////////////////////////////////
	@Override
	public Response createPlace(CreatePlaceRq place) {
		LOGGER.info("Calling Create Place API with request " + place.toString());
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
		LOGGER.info("Calling Read Places by City Name and ID API " +
					"with city name " + cityName + ", and city ID is " + cityID);
		try {
			List<PlaceItem> places = placeDomainService.getPlacesByCityNameAndID(cityName, cityID);
			ReadMultiplePlacesRs rmpRs = new ReadMultiplePlacesRs(places);
			return Response.ok(rmpRs).build();
		} catch (Exception e) {
			ReadMultiplePlacesRs rmpRs = new ReadMultiplePlacesRs(e);
			Status st = LocationServiceResultMapper.httpStatus(e);
			return Response.status(st).entity(rmpRs).build();
		}
	}

	@Override
	public Response readPlaceByPlaceID(Long placeID) {
		LOGGER.info("Calling Read Places by Place ID API with Place ID " + placeID);
		try {
			PlaceItem place = placeDomainService.getPlaceByPlaceID(placeID);
			ReadMultiplePlacesRs rmpRs = new ReadMultiplePlacesRs(place);
			return Response.ok(rmpRs).build();
		} catch (Exception e) {
			ReadMultiplePlacesRs rmpRs = new ReadMultiplePlacesRs(e);
			Status st = LocationServiceResultMapper.httpStatus(e);
			return Response.status(st).entity(rmpRs).build();
		}
	}

	@Override
	public Response readPlacesByKeyword(String placeKeyword,
			ReadPlacesByKeywordRq requestBody) {
		LOGGER.info("Calling Read Places by Keyword API with place key " + placeKeyword +
					" and whether partial matched as " + requestBody.isPartialMatched());
		try {
			List<PlaceItem> places = placeDomainService.getPlacesByKeyword(placeKeyword, requestBody.isPartialMatched());
			ReadMultiplePlacesRs rmpRs = new ReadMultiplePlacesRs(places);
			return Response.ok(rmpRs).build();
		} catch (Exception e) {
			ReadMultiplePlacesRs rmpRs = new ReadMultiplePlacesRs(e);
			Status st = LocationServiceResultMapper.httpStatus(e);
			return Response.status(st).entity(rmpRs).build();
		}
	}

	@Override
	public Response deletePlaceByID(Long placeID) {
		LOGGER.info("Calling Delete Place by Place ID with place ID " + placeID);
		try {
			placeDomainService.deletePaceByPlaceID(placeID);
			DeletePlaceRs dpRs = new DeletePlaceRs();
			return Response.ok(dpRs).build();
		} catch (Exception e) {
			DeletePlaceRs dpRs = new DeletePlaceRs(e);
			Status st = LocationServiceResultMapper.httpStatus(e);
			return Response.status(st).entity(dpRs).build();
		}
	}

}
