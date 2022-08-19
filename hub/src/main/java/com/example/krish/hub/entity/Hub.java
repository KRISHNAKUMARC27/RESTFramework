package com.example.krish.hub.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.krish.model.TMFResource;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonPropertyOrder({"id","href"})
@Entity
@Table(name="HUB")
@Getter
@Setter
@ToString
public class Hub extends TMFResource<Integer> {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name="USERCOL")
	private String user;
	
	@Column(name="ROLECOL")
	private String role;
	
	@Column(name="AGECOL")
	private String age;

	public Hub() {
		super();
	}
	
	public Hub(String user, String role, String age) {
		super();
		this.user = user;
		this.role = role;
		this.age = age;
	}
	
	
	@Override
	public Integer getPrimaryKey() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String getPrimaryKeyField() {
		// TODO Auto-generated method stub
		return "id";
	}

}
