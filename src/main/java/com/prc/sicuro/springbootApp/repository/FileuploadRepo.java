package com.prc.sicuro.springbootApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prc.sicuro.springbootApp.model.FileUpload;

public interface FileuploadRepo extends JpaRepository<FileUpload, Integer>{

	Optional<FileUpload> findById(Long id);

}
