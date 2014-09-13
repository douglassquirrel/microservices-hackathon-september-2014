package com.microserviceshack2.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Fact {
	@XmlElement
	private String desc;
	@XmlElement
	private String name;

	public Fact(String factName, String factcDesc) {
		this.name = factName;
		this.desc = factcDesc;
	}

	public String toJson() {
		return "{\"name\":\"" + name + "\","
				+ "\"description\":\"" + desc  + "\"" 
				+ "}";
	}
	
	

}
