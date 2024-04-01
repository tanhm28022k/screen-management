package com.example.screenmanagement.entity;

import com.example.screenmanagement.utility.Constant;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "Device")
@Data
public class Device {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status = Constant.STATUS.ACTIVE.name();

    @Column(name = "available")
    private Boolean available;

    @Column(name = "time_off_ago")
    private long timeOffAgo;

    @Column(name = "location")
    private String location;

    @Column(name = "identity_device")
    private String identityDevice;

    @Column(name = "userId")
    private String userId;

    @CreationTimestamp
    @Column(name = "ins_datetime")
    private Date insDatetime;

    @Column(name = "upd_datetime")
    private Date updDatetime;

}
