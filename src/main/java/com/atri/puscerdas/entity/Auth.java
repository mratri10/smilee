package com.atri.puscerdas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth")
public class Auth {
    @Id
    private String username;
    private String password;
    private String phone;
    private String email;
    private String token;

    @Column(name = "token_exp")
    private long tokenExp;

    private Integer role;
    private Integer status;
}
