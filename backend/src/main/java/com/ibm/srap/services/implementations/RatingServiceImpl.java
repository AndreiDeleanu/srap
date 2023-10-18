package com.ibm.srap.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.RatingDO;
import com.ibm.srap.models.Rating;
import com.ibm.srap.models.repositories.Ratings;
import com.ibm.srap.services.RatingService;

@Service("RatingService")
public class RatingServiceImpl implements RatingService {

	@Autowired Ratings ratings;
	
	
	@Override
	public List<RatingDO> getAllRatings() {
		List<RatingDO> response = new ArrayList<>();
		List<Rating> allRatings = ratings.findAll();
		if (allRatings == null) return response;
		
		for (Rating rating : allRatings) response.add(new ModelMapper().map(rating, RatingDO.class));
		
		return response;
	}

}
