package com.search.semantic.model.response;

import com.opencsv.bean.CsvBindByPosition;

public class Product {

	@CsvBindByPosition(position = 0)
	private String id;
	@CsvBindByPosition(position = 1)
	private String sKU;
	@CsvBindByPosition(position = 2)
	private String name;
	@CsvBindByPosition(position = 3)
	private boolean published;
	@CsvBindByPosition(position = 4)
	private String shortDescription;
	@CsvBindByPosition(position = 5)
	private String description;
	@CsvBindByPosition(position = 6)
	private boolean inStock;
	@CsvBindByPosition(position = 7)
	private int stock;
	@CsvBindByPosition(position = 8)
	private double salePrice;
	@CsvBindByPosition(position = 9)
	private double regularPrice;
	@CsvBindByPosition(position = 10)
	private String categories;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getsKU() {
		return sKU;
	}
	public void setsKU(String sKU) {
		this.sKU = sKU;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public boolean isInStock() {
		return inStock;
	}
	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}
	public double getRegularPrice() {
		return regularPrice;
	}
	public void setRegularPrice(double regularPrice) {
		this.regularPrice = regularPrice;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}

}
