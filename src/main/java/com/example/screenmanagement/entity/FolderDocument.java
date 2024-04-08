package com.example.screenmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "document_folder")
public class FolderDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "doc_id", nullable = false)
    private String docId;

    @Column(name = "folder_id", nullable = false)
    private String folderId;

    @Column(name = "ins_datetime")
    private Instant insDatetime;

    @Column(name = "upd_datetime")
    private Instant updDatetime;
}