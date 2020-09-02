package com.example.beatthestreet.api;

import com.example.beatthestreet.model.entity.EntityData;
import com.example.beatthestreet.requests.EntityRequest;
import com.example.beatthestreet.service.EntityDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/")
@RestController
public class EntityDataController {

	@Autowired
	private EntityDataService entityDataService;
	
	@GetMapping(path =  "{entitySymbol}")
    public @ResponseBody EntityData getEntityDataBySymbol(@PathVariable("entitySymbol") String entitySymbol, @RequestParam Map<String, String> allParams) throws Exception {
		EntityRequest entityRequest = new EntityRequest.EntityRequestBuilder()
											.setEntitySymbol(entitySymbol.toUpperCase())
											.setdataType(allParams.get("dataType"))
											.build();
    	return entityDataService.getEntityData(entityRequest);
    }
}