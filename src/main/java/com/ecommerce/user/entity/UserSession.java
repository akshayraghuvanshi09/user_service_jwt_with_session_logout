package com.ecommerce.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSession {
	@Id
	private String id;
	private Long userId;
	private LocalDateTime loginTme;
	private LocalDateTime lastAccessed;
	private String userName;

}
