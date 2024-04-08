package com.example.screenmanagement.repository;

import com.example.screenmanagement.entity.Document;
import com.example.screenmanagement.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, String>, JpaSpecificationExecutor<Document> {

    Folder findFirstById(String id);
}
