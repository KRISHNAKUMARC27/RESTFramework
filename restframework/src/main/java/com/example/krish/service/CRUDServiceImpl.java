package com.example.krish.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;

import com.example.krish.model.TMFException;
import com.example.krish.model.TMFExceptionInfo;
import com.example.krish.model.TMFResource;
import com.example.krish.model.attributefiltering.QueryElement;

public class CRUDServiceImpl<T extends TMFResource, ID> implements CRUDService<T,ID>{

	@Override
	public T create(T t, MultiValueMap<String, String> extras, Object... obj) throws Exception {
		// TODO Auto-generated method stub
		throw new TMFException(HttpStatus.NOT_IMPLEMENTED, new TMFExceptionInfo("ERR51", "Not Implemented", "Method not implemented"));
	}

	@Override
	public T update(T t, MultiValueMap<String, String> extras, Object... obj) throws Exception {
		// TODO Auto-generated method stub
		throw new TMFException(HttpStatus.NOT_IMPLEMENTED, new TMFExceptionInfo("ERR51", "Not Implemented", "Method not implemented"));
	}

	@Override
	public T findById(ID id, MultiValueMap<String, String> extras, Object... obj) throws Exception {
		// TODO Auto-generated method stub
		throw new TMFException(HttpStatus.NOT_IMPLEMENTED, new TMFExceptionInfo("ERR51", "Not Implemented", "Method not implemented"));
	}

	@Override
	public List<T> findAll(MultiValueMap<String, String> extras, Object... obj) throws Exception {
		// TODO Auto-generated method stub
		throw new TMFException(HttpStatus.NOT_IMPLEMENTED, new TMFExceptionInfo("ERR51", "Not Implemented", "Method not implemented"));
	}

	@Override
	public List<T> findAll(QueryElement queryElement, MultiValueMap<String, String> extras, Object... obj)
			throws Exception {
		// TODO Auto-generated method stub
		throw new TMFException(HttpStatus.NOT_IMPLEMENTED, new TMFExceptionInfo("ERR51", "Not Implemented", "Method not implemented"));
	}

	@Override
	public void deleteById(ID id, MultiValueMap<String, String> extras, Object... obj) throws Exception {
		// TODO Auto-generated method stub
		throw new TMFException(HttpStatus.NOT_IMPLEMENTED, new TMFExceptionInfo("ERR51", "Not Implemented", "Method not implemented"));
	}
	
	

}
