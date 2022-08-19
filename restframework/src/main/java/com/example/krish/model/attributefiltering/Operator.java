package com.example.krish.model.attributefiltering;


public enum Operator {

	AND (1, "AND", "&"),
	OR (2, "OR", ";"),
	NOT (3, "NOT", "!"),
	EQ (4, "EQ", "="),
	LT (5, "LT", "<"),
	LTE (6, "LTE", "<="),
	GT (7, "GT", ">"),
	GTE (8, "GTE", ">="),
	NE (9, "NT", "!="),
	VALUE_SEP(10, "VALUE_SEP", ",") //SPECIAL CONDITION FOR HTML PARAM PARSING
	;
	
	private int id;
	private String repr;
	private String htmlrepr;
	
	Operator(int id, String repr, String htmlrepr) {
		this.id = id;
		this.repr = repr;
		this.htmlrepr = htmlrepr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRepr() {
		return repr;
	}

	public void setRepr(String repr) {
		this.repr = repr;
	}

	public String getHtmlrepr() {
		return htmlrepr;
	}

	public void setHtmlrepr(String htmlrepr) {
		this.htmlrepr = htmlrepr;
	}
	
	
	
}
