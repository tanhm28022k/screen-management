package com.example.screenmanagement.entity;

import com.example.screenmanagement.utility.Constant;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "fullname", length = 150, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Constant.GENDER gender;

    @Column(name = "mail", length = 150)
    private String mail;

    @Column(name = "phone_numb", length = 10, nullable = false, unique = true)
    private String phoneNumb;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "roles")
    private String roles;

    @Column(name = "status")
    private String status = Constant.STATUS.ACTIVE.name();

    @CreationTimestamp
    @Column(name = "ins_datetime")
    private Date insDatetime;

    @UpdateTimestamp
    @Column(name = "upd_datetime")
    private Date updDatetime;

}
