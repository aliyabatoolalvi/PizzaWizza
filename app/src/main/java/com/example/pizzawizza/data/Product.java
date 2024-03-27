package com.example.pizzawizza.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Product{

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("subCategory")
	private String subCategory;

	@SerializedName("discountedPrice")
	private int discountedPrice;

	@SerializedName("price")
	private int price;

	@SerializedName("name")
	private String name;

	@SerializedName("rating")
	private Float rating;

	@SerializedName("details")
	private String details;

	@PrimaryKey
	@SerializedName("id")
	private int id;

	@SerializedName("shortDescription")
	private String shortDescription;

	@SerializedName("category")
	private String category;

	@SerializedName("picture")
	private String picture;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setSubCategory(String subCategory){
		this.subCategory = subCategory;
	}

	public String getSubCategory(){
		return subCategory;
	}

	public void setDiscountedPrice(int discountedPrice){
		this.discountedPrice = discountedPrice;
	}

	public int getDiscountedPrice(){
		return discountedPrice;
	}

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setRating(Float rating){
		this.rating = rating;
	}

	public Float getRating(){
		return rating;
	}

	public void setDetails(String details){
		this.details = details;
	}

	public String getDetails(){
		return details;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setShortDescription(String shortDescription){
		this.shortDescription = shortDescription;
	}

	public String getShortDescription(){
		return shortDescription;
	}

	public void setCategory(String category){
		this.category = category;
	}

	public String getCategory(){
		return category;
	}

	public void setPicture(String picture){
		this.picture = picture;
	}

	public String getPicture(){
		return picture;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}