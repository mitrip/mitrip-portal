package com.kbmstudio.opencms.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="user")
public class User {
    @Id
    @Column(name="id") 
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name="email")
    private String email;
    
    @Column(name="password")
    private String password;
    
    @Column(name="name")
    private String name;
    
    @Column(name="is_confirm")
    private Boolean confirm;

    public Long getId() {
            return id;
    }

    public void setId(Long id) {
            this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
        
        
	
}
