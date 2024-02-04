package com.atri.puscerdas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String token_exp;
    private Integer role;
    private Integer status;
}
