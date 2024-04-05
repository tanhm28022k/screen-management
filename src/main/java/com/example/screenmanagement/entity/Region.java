package com.example.screenmanagement.entity;

import com.example.screenmanagement.utility.Constant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "region")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "status")
    private String status = Constant.STATUS.ACTIVE.name();

    @CreationTimestamp
    @Column(name = "ins_datetime")
    private Date insDatetime;

    @UpdateTimestamp
    @Column(name = "upd_datetime")
    private Date updDatetime;

}