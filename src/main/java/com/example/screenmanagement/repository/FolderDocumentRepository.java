package com.example.screenmanagement.repository;

import com.example.screenmanagement.entity.Document;
import com.example.screenmanagement.entity.FolderDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderDocumentRepository extends JpaRepository<FolderDocument, String>, JpaSpecificationExecutor<Document> {
}
