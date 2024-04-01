package com.example.screenmanagement.repository;

import com.example.screenmanagement.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DocumentRepository extends JpaRepository<Document, String>, JpaSpecificationExecutor<Document> {

}
