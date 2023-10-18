package com.ibm.srap.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.srap.models.Role;

public interface Roles extends JpaRepository<Role, Integer> {
	
	/**
	 * @param name the name of a Role entity
	 * @return the matching Role entity
	 */
	Role findByName(String name);
	
	/**
	 * @param name the name of a Role
	 * @return ID of the Role entity
	 */
	@Query("SELECT r.id FROM Role r WHERE r.name = :name")
	Integer getRoleIdByName(@Param("name") String name);
	
}
