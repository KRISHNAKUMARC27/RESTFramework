package com.example.krish.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.krish.model.TMFException;
import com.example.krish.model.TMFExceptionInfo;
import com.example.krish.model.TMFResource;
import com.example.krish.model.attributefiltering.QueryElement;
import com.example.krish.service.CRUDService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CRUDController<T extends TMFResource, Id> extends AbstractController {

	protected CRUDService<T, Id> crudService;

	//private static final Logger logger = LogManager.getLogger(CRUDController.class);

	public CRUDController(CRUDService<T, Id> crudService) {
		this.crudService = crudService;
	}

	public void setHref(T t) {
		log.trace("#### Entering setHref ####");
		t.setHref(uriPath + "/" + t.getPrimaryKey());
		log.trace("#### Exting setHref ####");
	}

	@GetMapping("/{id}")
	public MappingJacksonValue findById(HttpServletRequest request, HttpServletResponse response,
			@RequestHeader HttpHeaders headers, @PathVariable Id id,
			@RequestParam(required = false) MultiValueMap<String, String> params) throws Exception {
//		log.traceEntry();

		if (!checkRestgwStatus()) {
			log.debug("Traffic Inhibited for all GET by ID operations!!!");
			throw new TMFException(HttpStatus.SERVICE_UNAVAILABLE, new TMFExceptionInfo("service_unavailable"));
		}

		T t = crudService.findById(id, headers);

		setHref(t);
		String fields = null;

		return getMappingJacksonValue(t, fields);
	}

	@GetMapping
	public MappingJacksonValue findAll(@RequestParam(required = false) MultiValueMap<String, String> params,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader HttpHeaders headers)
			throws Exception {
		
	//	log.traceEntry();
		log.trace("#### Entering CRUDController findAll ####");
		if (!checkRestgwStatus()) {
			log.debug("Traffic Inhibited for all GET by ID operations!!!");
			throw new TMFException(HttpStatus.SERVICE_UNAVAILABLE, new TMFExceptionInfo("service_unavailable"));
		}
		String fields = null;
		
		String query = getQuery(params);
		List<T> listOfResources = null;
		QueryElement queryElement = QueryElement.parse(query);
		log.trace("queryElement is " + queryElement);
		if(query.trim().length() > 0 && queryElement != null) {
			listOfResources = crudService.findAll(queryElement, headers);
		} else {
			listOfResources = crudService.findAll(headers);
		}
		
		for(T t: listOfResources) {
			setHref(t);
		}
		
		return getMappingJacksonValueForList(listOfResources, fields);
	}

	@PostMapping
	public ResponseEntity<MappingJacksonValue> create(HttpServletRequest request, HttpServletResponse response,
			@RequestHeader HttpHeaders headers, @RequestBody T t, UriComponentsBuilder b,
			@RequestParam(required = false) String fields) throws Exception {
//		log.traceEntry();

		if (!checkRestgwStatus()) {
			log.debug("Traffic Inhibited for all POST operations!!!");
			throw new TMFException(HttpStatus.SERVICE_UNAVAILABLE, new TMFExceptionInfo("service_unavailable"));
		}

		T responseObj = crudService.create(t, headers);
		UriComponents uriComponents = b.path(uriPath + "/{id}").buildAndExpand(t.getPrimaryKey());
		setHref(t);

		return ResponseEntity.created(uriComponents.toUri()).body(getMappingJacksonValue(t, fields));
	}
	
	@PutMapping(path= "/{id}")
	public MappingJacksonValue update(HttpServletRequest request, HttpServletResponse response,
			@RequestHeader HttpHeaders headers, @RequestBody T t, @PathVariable Id id, UriComponentsBuilder b
			) throws Exception {
	//	log.traceEntry();

		if (!checkRestgwStatus()) {
			log.debug("Traffic Inhibited for all PUT operations!!!");
			throw new TMFException(HttpStatus.SERVICE_UNAVAILABLE, new TMFExceptionInfo("service_unavailable"));
		}

		if(!(t.getPrimaryKey() != null && t.getPrimaryKey().equals(id)))
			throw new IllegalArgumentException();
		T responseObj = crudService.update(t, headers);
		
		setHref(t);

		return getMappingJacksonValue(t, null);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(HttpServletRequest request, HttpServletResponse response,
			@RequestHeader HttpHeaders headers, @PathVariable Id id
			) throws Exception {
	//	log.traceEntry();

		if (!checkRestgwStatus()) {
			log.debug("Traffic Inhibited for all DELETE by ID operations!!!");
			throw new TMFException(HttpStatus.SERVICE_UNAVAILABLE, new TMFExceptionInfo("service_unavailable"));
		}

		crudService.deleteById(id, headers);

		
		return ResponseEntity.noContent().build();
	}

	public String getQuery(MultiValueMap<String, String> params) {
		StringBuffer queryBuilder = new StringBuffer();
		String query = null;
		for(String key : params.keySet()) {
			List<String> values = params.get(key);
			for(String value : values) {
				queryBuilder.append(key + "=" + value + "&");
			}
		}
		if(queryBuilder.toString().endsWith("=&")) {
			query = queryBuilder.toString().substring(0, queryBuilder.length() -2);
		} else if(queryBuilder.toString().endsWith("&")) {
			query = queryBuilder.toString().substring(0, queryBuilder.length() -1);
		} else {
			query = queryBuilder.toString();
		}
		return query;
	}

	protected MappingJacksonValue getMappingJacksonValue(T t, String fields) {
		log.trace("#### Entering getMappingJacksonValue ####");
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(t);
		mappingJacksonValue.setFilters(getMappingJacksonFilter(fields, t.getPrimaryKeyField()));
		log.trace("#### Exting getMappingJacksonValue ####");

		return mappingJacksonValue;
	}
	
	protected MappingJacksonValue getMappingJacksonValueForList(List<T> aList, String fields) {
		log.trace("#### Entering getMappingJacksonValueForList ####");
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(aList);
		String primaryKeyField = aList.size() > 0 ? aList.get(0).getPrimaryKeyField() : null;
		
		mappingJacksonValue.setFilters(getMappingJacksonFilter(fields, primaryKeyField));
		log.trace("#### Exting getMappingJacksonValueForList ####");

		return mappingJacksonValue;
	}
	
	protected FilterProvider getMappingJacksonFilter(String fields, String primaryKeyField) {
		log.trace("#### Entering getMappingJacksonFilter ####");
		SimpleBeanPropertyFilter simpleBeanPropertyFilter = null;
		if (fields != null && fields.equalsIgnoreCase("none")) {
			simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept(primaryKeyField, "href");
		} else if (fields != null) {
			Set<String> fieldSet = new HashSet<>();
			String[] tokens = fields.split(",");
			if (primaryKeyField != null)
				fieldSet.add(primaryKeyField);
			fieldSet.add("href");
			for (String token : tokens) {
				fieldSet.add(token);
			}

			simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept(fieldSet);
		} else {
			simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAll();
		}
		SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
		simpleFilterProvider.setFailOnUnknownId(false);
		FilterProvider filterProvider = simpleFilterProvider.addFilter("crudFilter", simpleBeanPropertyFilter);
		log.trace("#### Exting getMappingJacksonFilter ####");

		return filterProvider;
	}

}
