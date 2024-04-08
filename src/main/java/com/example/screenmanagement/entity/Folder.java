package com.example.screenmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "folder")
@Data
public class Folder {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "nam", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "path")
    private String path;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "status")
    private String status;

    @Column(name = "ins_datetime")
    private Date insDatetime;

    @Column(name = "upd_datetime")
    private Date updDatetime;

}