package com.xyw55.po;

import java.sql.*;
import java.util.*;

public class Notes {

	private java.sql.Date createdAt;
	private java.sql.Date createTime;
	private String author;
	private String tag;
	private java.math.BigInteger id;
	private String title;
	private String content;
	private java.sql.Date updatedAt;


	public java.sql.Date getCreatedAt(){ 
		return createdAt;
	}
	public java.sql.Date getCreateTime(){ 
		return createTime;
	}
	public String getAuthor(){ 
		return author;
	}
	public String getTag(){ 
		return tag;
	}
	public java.math.BigInteger getId(){ 
		return id;
	}
	public String getTitle(){ 
		return title;
	}
	public String getContent(){ 
		return content;
	}
	public java.sql.Date getUpdatedAt(){ 
		return updatedAt;
	}


	public void setCreatedAt(java.sql.Date createdAt){
		this.createdAt = createdAt;
	}
	public void setCreateTime(java.sql.Date createTime){
		this.createTime = createTime;
	}
	public void setAuthor(String author){
		this.author = author;
	}
	public void setTag(String tag){
		this.tag = tag;
	}
	public void setId(java.math.BigInteger id){
		this.id = id;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public void setContent(String content){
		this.content = content;
	}
	public void setUpdatedAt(java.sql.Date updatedAt){
		this.updatedAt = updatedAt;
	}


}