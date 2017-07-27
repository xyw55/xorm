package com.xyw55.po;

import java.sql.*;
import java.util.*;

public class Users {

	private java.sql.Date createdAt;
	private String password;
	private java.sql.Date createTime;
	private Long id;
	private String username;
	private java.sql.Date updatedAt;


	public java.sql.Date getCreatedAt(){ 
		return createdAt;
	}
	public String getPassword(){ 
		return password;
	}
	public java.sql.Date getCreateTime(){ 
		return createTime;
	}
	public Long getId(){ 
		return id;
	}
	public String getUsername(){ 
		return username;
	}
	public java.sql.Date getUpdatedAt(){ 
		return updatedAt;
	}


	public void setCreatedAt(java.sql.Date createdAt){
		this.createdAt = createdAt;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public void setCreateTime(java.sql.Date createTime){
		this.createTime = createTime;
	}
	public void setId(Long id){
		this.id = id;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public void setUpdatedAt(java.sql.Date updatedAt){
		this.updatedAt = updatedAt;
	}


}