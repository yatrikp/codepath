package com.codepath.googleimagesearch.models;

import java.io.Serializable;

public class ImageFilter implements Serializable{

	private static final long serialVersionUID = 7251932076288855021L;
	private String size = null; // (small, medium, large, extra-large)
	private String color = null; // filter (black, blue, brown, gray, green, etc...)
	private String type = null; // (faces, photo, clip art, line art)
	private String site = null; // (espn.com)
	
	public ImageFilter(){		
	}
	
	public ImageFilter(String size, String color, String type, String site) {
		this.size = size;
		this.color = color;
		this.type = type;
		this.site = site;
	}
	
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	
	
}
