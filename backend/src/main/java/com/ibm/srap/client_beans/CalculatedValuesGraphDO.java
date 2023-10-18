package com.ibm.srap.client_beans;

public class CalculatedValuesGraphDO {

	private int unsatCount;
	private int marginalCount;
	private int satCount;
	private int notCalculated;

	public int getUnsatCount() {
		return unsatCount;
	}

	public void setUnsatCount(int unsatCount) {
		this.unsatCount = unsatCount;
	}

	public int getMarginalCount() {
		return marginalCount;
	}

	public void setMarginalCount(int marginalCount) {
		this.marginalCount = marginalCount;
	}

	public int getSatCount() {
		return satCount;
	}

	public void setSatCount(int satCount) {
		this.satCount = satCount;
	}

	public int getNotCalculated() {
		return notCalculated;
	}

	public void setNotCalculated(int notCalculated) {
		this.notCalculated = notCalculated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + marginalCount;
		result = prime * result + notCalculated;
		result = prime * result + satCount;
		result = prime * result + unsatCount;
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
		CalculatedValuesGraphDO other = (CalculatedValuesGraphDO) obj;
		if (marginalCount != other.marginalCount)
			return false;
		if (notCalculated != other.notCalculated)
			return false;
		if (satCount != other.satCount)
			return false;
		if (unsatCount != other.unsatCount)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CalculatedValuesGraphDO [unsatCount=" + unsatCount + ", marginalCount=" + marginalCount + ", satCount="
				+ satCount + ", notCalculated=" + notCalculated + "]";
	}

}
