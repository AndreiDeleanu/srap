package com.ibm.srap.client_beans;

import java.util.List;

public class SearchFilterParamsDO {

	private Integer domainId;
	private List<String> processNames;
	private List<String> ratings;
	private List<String> subdomainNames;
	private List<String> squadNames;

	public Integer getDomainId() {
		return domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	public List<String> getRatings() {
		return ratings;
	}

	public void setRatings(List<String> ratings) {
		this.ratings = ratings;
	}

	public List<String> getSubdomainNames() {
		return subdomainNames;
	}

	public void setSubdomainNames(List<String> subdomainNames) {
		this.subdomainNames = subdomainNames;
	}

	public List<String> getSquadNames() {
		return squadNames;
	}

	public void setSquadNames(List<String> squadNames) {
		this.squadNames = squadNames;
	}

	public List<String> getProcessNames() {
		return processNames;
	}

	public void setProcessNames(List<String> processNames) {
		this.processNames = processNames;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domainId == null) ? 0 : domainId.hashCode());
		result = prime * result + ((processNames == null) ? 0 : processNames.hashCode());
		result = prime * result + ((ratings == null) ? 0 : ratings.hashCode());
		result = prime * result + ((squadNames == null) ? 0 : squadNames.hashCode());
		result = prime * result + ((subdomainNames == null) ? 0 : subdomainNames.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchFilterParamsDO other = (SearchFilterParamsDO) obj;
		if (domainId == null) {
			if (other.domainId != null)
				return false;
		} else if (!domainId.equals(other.domainId))
			return false;
		if (processNames == null) {
			if (other.processNames != null)
				return false;
		} else if (!processNames.equals(other.processNames))
			return false;
		if (ratings == null) {
			if (other.ratings != null)
				return false;
		} else if (!ratings.equals(other.ratings))
			return false;
		if (squadNames == null) {
			if (other.squadNames != null)
				return false;
		} else if (!squadNames.equals(other.squadNames))
			return false;
		if (subdomainNames == null) {
			if (other.subdomainNames != null)
				return false;
		} else if (!subdomainNames.equals(other.subdomainNames))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SearchFilterParamsDO [domainId=" + domainId + ", processNames=" + processNames + ", ratings=" + ratings
				+ ", subdomainNames=" + subdomainNames + ", squadNames=" + squadNames + "]";
	}

}
