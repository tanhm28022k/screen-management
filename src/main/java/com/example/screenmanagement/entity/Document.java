package com.example.screenmanagement.entity;

import com.example.screenmanagement.utility.Constant;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "document")
@Data
public class Document {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "status")
    private String status = Constant.STATUS.ACTIVE.name();

    @CreationTimestamp
    @Column(name = "ins_datetime")
    private Date insDatetime;

    @UpdateTimestamp
    @Column(name = "upd_datetime")
    private Date updDatetime;

}
