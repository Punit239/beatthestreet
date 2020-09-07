package com.example.beatthestreet.api;

import com.example.beatthestreet.model.entity.EntityData;
import com.example.beatthestreet.model.entity.EntityFinancials;
import com.example.beatthestreet.model.entity.EntityPublicInfo;
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
    public @ResponseBody EntityData getEntityData(@PathVariable("entitySymbol") String entitySymbol) throws Exception {
		EntityRequest entityRequest = new EntityRequest.EntityRequestBuilder()
											.setEntitySymbol(entitySymbol.toUpperCase())
											.build();
    	return entityDataService.getEntityData(entityRequest);
    }

	@GetMapping(path =  "{entitySymbol}/financials")
	public @ResponseBody EntityFinancials getEntityFiancials(@PathVariable("entitySymbol") String entitySymbol, @RequestParam Map<String, String> allParams) throws Exception {
		EntityRequest entityRequest = new EntityRequest.EntityRequestBuilder()
				.setEntitySymbol(entitySymbol.toUpperCase())
				.setPeriod(allParams.get("period"))
				.build();
		return entityDataService.getEntityFinancials(entityRequest);
	}

	@GetMapping(path =  "{entitySymbol}/info")
	public @ResponseBody
	EntityPublicInfo getEntityPublicInfo(@PathVariable("entitySymbol") String entitySymbol) throws Exception {
		EntityRequest entityRequest = new EntityRequest.EntityRequestBuilder()
				.setEntitySymbol(entitySymbol.toUpperCase())
				.build();
		return entityDataService.getEntityPublicInfo(entityRequest);
	}
}