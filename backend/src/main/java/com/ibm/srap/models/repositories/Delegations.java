package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.srap.models.Delegation;

public interface Delegations extends JpaRepository<Delegation, Integer> {
	
	/**
	 * @param delegate
	 * @param delegator
	 * @return All delegation entities that have either the specified delegate, OR the specified delegator
	 */
	List<Delegation> findAllByDelegateOrDelegator(String delegate, String delegator);
	
	/**
	 * @param delegate
	 * @return All delegation entities that have the specified delegate
	 */
	List<Delegation> findAllByDelegate(String delegate);
	
	/**
	 * @param userRoleId The user role ID for which a delegation is created
	 * @return All delegation entities for the specified role
	 */
	List<Delegation> findAllByRoleId(Integer userRoleId);

}
