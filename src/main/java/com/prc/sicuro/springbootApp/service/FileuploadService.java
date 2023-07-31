package com.prc.sicuro.springbootApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prc.sicuro.springbootApp.dao.FileuploadDao;
import com.prc.sicuro.springbootApp.model.FileUpload;

@Service
public class FileuploadService {
    
	@Autowired
	private FileuploadDao fileuploadDao;
	
	public FileUpload saveFile(FileUpload fileUpload) {
		return fileuploadDao.saveFile(fileUpload);
		
	}
}
