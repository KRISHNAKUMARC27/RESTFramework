package com.example.krish.model.attributefiltering;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchCriteria extends QueryElement {

	private String lhs;
	private Operator operation;
	private String rhs;

	public SearchCriteria(String lhs, Operator operation, String rhs) {
		super();
		this.lhs = lhs;
		this.operation = operation;
		this.rhs = rhs;
	}

	public String getLhs() {
		return lhs;
	}

	public void setLhs(String lhs) {
		this.lhs = lhs;
	}

	public Operator getOperation() {
		return operation;
	}

	public void setOperation(Operator operation) {
		this.operation = operation;
	}

	public String getRhs() {
		return rhs;
	}

	public void setRhs(String rhs) {
		this.rhs = rhs;
	}

	@Override
	public String toString() {
		return "SearchCriteria [lhs=" + lhs + ", operation=" + operation + ", rhs=" + rhs + "]";
	}

	private static Operator[] validOps = { Operator.LTE, Operator.GTE, Operator.EQ, Operator.LT, Operator.GT };
	private static final Logger logger = LogManager.getLogger(SearchCriteria.class);

	public static QueryElement parse(String input) {
		logger.trace("#### Entering SearchCriteria parse ####");

		String lhs = null;
		String rhs = null;

		for (Operator oper : validOps) {
			int index = input.indexOf(oper.getHtmlrepr());
			if (index > -1) {
				lhs = input.substring(0, index);
				rhs = input.substring(index + oper.getHtmlrepr().length());
				if (oper.equals(Operator.EQ)) {
					String[] tokens = rhs.split(",");
					if (tokens.length > 1) {
						QueryElement queryElement = null;
						for (String token : tokens) {
							if (queryElement == null) {
								queryElement = new SearchCriteria(lhs, oper, token);
							} else {
								SearchCriteria sc = new SearchCriteria(lhs, oper, token);
								queryElement = new LogicalCondition(queryElement, Operator.OR, sc);
							}
						}
						logger.trace("#### Exiting search criteria parse ####");
						return queryElement;
					}
				}
				logger.trace("#### Exiting search criteria parse ####");
				return new SearchCriteria(lhs, oper, rhs);
			}
		}
		logger.trace("#### Exiting search criteria parse ####");
		return null;
	}

}
