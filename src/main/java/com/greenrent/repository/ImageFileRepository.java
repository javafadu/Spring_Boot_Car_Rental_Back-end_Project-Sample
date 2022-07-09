package com.greenrent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greenrent.domain.ImageFile;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, String> {

}