package com.kbmstudio.opencms.hibernate.model;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="user_destination")
public class UserDestination {
    @Id
    @Column(name="id") 
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name="destination")
    private String destination;
    
    @Column(name="user_id")
    private String userId;
    
    @Column(name="start_date")
    private Date startDate;
    
    @Column(name="end_date")
    private Date endDate;
    
        
    public Long getId() {
            return id;
    }

    public void setId(Long id) {
            this.id = id;
    }

   
        
	
}
