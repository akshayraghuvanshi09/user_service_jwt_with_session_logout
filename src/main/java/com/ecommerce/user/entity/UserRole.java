package com.ecommerce.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "roles")
public class UserRole {

	@Id
	@Column(name = "roll_id")
	private Integer rid;
	
	@Column(name = "roll_name")
	private String rname;
}
