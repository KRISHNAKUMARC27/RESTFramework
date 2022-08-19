package com.example.krish.model.attributefiltering;

public class LogicalCondition extends QueryElement {
	private QueryElement lhs;
	private Operator operator;
	private QueryElement rhs;

	public LogicalCondition(QueryElement lhs, Operator operator, QueryElement rhs) {
		super();
		if(!(operator == Operator.AND || operator == Operator.OR))
			throw new IllegalArgumentException("Only AND and OR allowed");
		this.lhs = lhs;
		this.operator = operator;
		this.rhs = rhs;
	}

	public QueryElement getLhs() {
		return lhs;
	}

	public void setLhs(QueryElement lhs) {
		this.lhs = lhs;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public QueryElement getRhs() {
		return rhs;
	}

	public void setRhs(QueryElement rhs) {
		this.rhs = rhs;
	}

	@Override
	public String toString() {
		return "LogicalCondition [lhs=" + lhs + ", operator=" + operator + ", rhs=" + rhs + "]";
	}
	
	
	
}
