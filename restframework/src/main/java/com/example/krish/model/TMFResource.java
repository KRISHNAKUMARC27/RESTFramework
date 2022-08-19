package com.example.krish.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonFilter("crudFilter")
public abstract class TMFResource<ID> {
	public String href;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	@JsonIgnore
	public abstract ID getPrimaryKey();
	
	@JsonIgnore
	public abstract String getPrimaryKeyField();

	@Override
	public String toString() {
		return "TMFResource [href=" + href + "]";
	}
	
	
	
}
