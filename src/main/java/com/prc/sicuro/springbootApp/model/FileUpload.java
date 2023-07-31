package com.prc.sicuro.springbootApp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class FileUpload {
    
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	public Long id;
    public String imagename;
	
    @ManyToOne
	@JoinColumn
	private User user;
   
	public FileUpload() {
	
	}

	public FileUpload(Long id,String imagename, User user) {
		super();
		this.id=id;
		this.imagename = imagename;
		this.user = user;
	}

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImagename() {
		return imagename;
	}

	public void setImagename(String imagename) {
		this.imagename = imagename;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	
	
}
