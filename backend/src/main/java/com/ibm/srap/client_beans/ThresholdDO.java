package com.ibm.srap.client_beans;

public class ThresholdDO {

	private Integer id;
	private Integer domainId;
	private Integer satThreshold;
	private Integer marginalThreshold;
	private String quarter;
	private String year;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	public Integer getSatThreshold() {
		return satThreshold;
	}

	public void setSatThreshold(Integer satThreshold) {
		this.satThreshold = satThreshold;
	}

	public Integer getMarginalThreshold() {
		return marginalThreshold;
	}

	public void setMarginalThreshold(Integer marginalThreshold) {
		this.marginalThreshold = marginalThreshold;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domainId == null) ? 0 : domainId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((marginalThreshold == null) ? 0 : marginalThreshold.hashCode());
		result = prime * result + ((quarter == null) ? 0 : quarter.hashCode());
		result = prime * result + ((satThreshold == null) ? 0 : satThreshold.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
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
		ThresholdDO other = (ThresholdDO) obj;
		if (domainId == null) {
			if (other.domainId != null)
				return false;
		} else if (!domainId.equals(other.domainId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (marginalThreshold == null) {
			if (other.marginalThreshold != null)
				return false;
		} else if (!marginalThreshold.equals(other.marginalThreshold))
			return false;
		if (quarter == null) {
			if (other.quarter != null)
				return false;
		} else if (!quarter.equals(other.quarter))
			return false;
		if (satThreshold == null) {
			if (other.satThreshold != null)
				return false;
		} else if (!satThreshold.equals(other.satThreshold))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ThresholdDO [id=" + id + ", domainId=" + domainId + ", satThreshold=" + satThreshold + ", marginalThreshold="
				+ marginalThreshold + ", quarter=" + quarter + ", year=" + year + "]";
	}

}
