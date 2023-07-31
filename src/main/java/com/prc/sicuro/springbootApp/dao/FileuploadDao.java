package com.prc.sicuro.springbootApp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.prc.sicuro.springbootApp.model.FileUpload;
import com.prc.sicuro.springbootApp.repository.FileuploadRepo;

@Repository
public class FileuploadDao {
     
    @Autowired
	private FileuploadRepo fileuploadRepo;
    
    public FileUpload saveFile(FileUpload fileUpload) {
	   return	fileuploadRepo.save(fileUpload);
	}
    
}
