package in.complyease.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.*;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {
	
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name = "is_deleted")
	private boolean isDeleted = false;
	
}
