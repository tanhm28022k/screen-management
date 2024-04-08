package com.example.screenmanagement.entity;

import com.example.screenmanagement.utility.Constant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "Device")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(name = "location")
    private String location;

    @Column(name = "time_off_ago")
    private long timeOffAgo;

    @Column(name = "model")
    private String model;

    @Column(name = "identity_device")
    private String identityDevice;

    @Column(name = "userId")
    private String userId;

    @Column(name = "is_presenting")
    private String isPresenting;

    @Column(name = "is_presenting_schedule")
    private String isPresentingSchedule;

    @Column(name = "os_version")
    private String osVersion;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "last_active_time")
    private Date lastActiveTime;

    @Column(name = "last_disconnected_time")
    private Date lastDisconnectedTime;

    @CreationTimestamp
    @Column(name = "ins_datetime")
    private Date insDatetime;

    @Column(name = "upd_datetime")
    private Date updDatetime;

    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

}
