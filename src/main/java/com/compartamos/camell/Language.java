package com.compartamos.camell;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Language {
    
    private int id;
    private String name;
    private String description;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    

}
