package com.example.krish.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.krish.controller.CRUDController;
import com.example.krish.hub.entity.Hub;
import com.example.krish.hub.service.HubService;

@RestController
@RequestMapping({"${hub.path:/hub}"})
public class HubController extends CRUDController<Hub,Integer>{
	
	protected HubService hubService;
	
	@Autowired
	public HubController(HubService crudService, @Value("${hub.path:/hub}") String path) {
		super(crudService);
		hubService = crudService;
		uriPath = path;
		appStatusProperty = "restgw.hub.status";
	}

}
