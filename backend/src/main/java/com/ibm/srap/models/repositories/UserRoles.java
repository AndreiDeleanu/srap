package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.srap.models.UserRole;

public interface UserRoles extends JpaRepository<UserRole, Integer> {
	
	/**
	 * @param user
	 * @return
	 */
	@Query("SELECT DISTINCT ur.role.name FROM UserRole ur WHERE ur.userMail = :user")
	List<String> getRolesByUser(@Param("user") String user);
	
	/**
	 * @param user
	 * @return
	 */
	@Query("SELECT DISTINCT ur.domain.id FROM UserRole ur WHERE ur.userMail = :user")
	List<Integer> findDomainIdByUser(@Param("user") String user);
	
	/**
	 * @param user
	 * @param domainId
	 * @return
	 */
	@Query("SELECT DISTINCT ur.role.name FROM UserRole ur WHERE ur.userMail = :user AND ur.domain.id = :domain")
	List<String> getRolesByUserAndDomain(@Param("user") String user, @Param("domain") Integer domainId);
	
	/**
	 * @param domainId
	 * @param role
	 * @param user
	 * @return
	 */
	boolean existsByDomainIdAndRoleIdAndUserMail (Integer domainId, Integer role, String user);
	
	/**
	 * @param user
	 * @param role
	 * @return
	 */
	UserRole findOneByUserMailAndRoleName(String user, String role);
	
	
}
