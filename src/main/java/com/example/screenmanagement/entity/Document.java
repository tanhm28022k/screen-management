package com.example.screenmanagement.entity;

import com.example.screenmanagement.utility.Constant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "document")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "size")
    private Long size;

    @Column(name = "type")
    private String type;

    @Column(name = "uploaded_time")
    private Long uploadedTime;

    @Column(name = "status")
    private String status = Constant.STATUS.ACTIVE.name();

    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE ;

    @Column(name = "user_id")
    private String userId;

    @CreationTimestamp
    @Column(name = "ins_datetime")
    private Date insDatetime;

    @UpdateTimestamp
    @Column(name = "upd_datetime")
    private Date updDatetime;

}
