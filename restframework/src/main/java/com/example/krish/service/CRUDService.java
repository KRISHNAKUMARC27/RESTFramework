package com.example.krish.service;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.example.krish.model.TMFResource;
import com.example.krish.model.attributefiltering.QueryElement;

public interface CRUDService<T extends TMFResource, ID> {

	public T create(T t, MultiValueMap<String, String> extras, Object... obj) throws Exception;
	public T update(T t, MultiValueMap<String, String> extras, Object... obj) throws Exception;
	public T findById(ID id, MultiValueMap<String, String> extras, Object... obj) throws Exception;
	public List<T> findAll(MultiValueMap<String, String> extras, Object... obj) throws Exception;
	public List<T> findAll(QueryElement queryElement, MultiValueMap<String, String> extras, Object... obj) throws Exception;
	
	public void deleteById(ID id, MultiValueMap<String, String> extras, Object... obj) throws Exception;
	
}
