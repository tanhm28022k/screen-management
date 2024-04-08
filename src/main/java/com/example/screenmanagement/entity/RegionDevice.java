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
@Table(name = "region_device")
public class RegionDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "region_id", nullable = false)
    private String regionId;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "ins_datetime")
    private Instant insDatetime;

    @Column(name = "upd_datetime")
    private Instant updDatetime;
}