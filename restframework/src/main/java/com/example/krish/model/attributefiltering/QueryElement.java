package com.example.krish.model.attributefiltering;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class QueryElement {
	private static final Logger logger = LogManager.getLogger(QueryElement.class);

	public static QueryElement parse(String htmlExp) {
		logger.trace("#### Entering QueryElement parse ####");

		if (htmlExp == null || htmlExp.trim().isEmpty())
			return null;

		htmlExp = htmlExp.trim();
		String[] andConditions = htmlExp.split(Operator.AND.getHtmlrepr());
		QueryElement result = null;
		List<QueryElement> andList = new ArrayList<>();

		for (String andCondition : andConditions) {
			String[] orConditions = andCondition.split(Operator.OR.getHtmlrepr());
			QueryElement orResult = null;
			for (String orConditon : orConditions) {
				QueryElement element = SearchCriteria.parse(orConditon);
				if (orResult == null) {
					orResult = element;
				} else {
					orResult = new LogicalCondition(orResult, Operator.OR, element);
				}
			}
			if (result == null) {
				result = orResult;
			} else {
				result = new LogicalCondition(result, Operator.AND, orResult);
			}
		}
		logger.trace("#### Exiting QueryElement parse ####");
		return result;

	}
	
	public static void main(String[] args) throws Exception{
		String input = "a>5;c>=5&d>=1;e=10,11,12";
		System.out.println(input);
		System.out.println(parse(input));
	}
}
