package com.ibm.srap.client_beans;

public class FLLRatingGraphDO {

	private int unsatCount;
	private int marginalCount;
	private int satCount;
	private int naCount;

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

	public int getNaCount() {
		return naCount;
	}

	public void setNaCount(int naCount) {
		this.naCount = naCount;
	}

	@Override
	public String toString() {
		return "FLLRatingGraphDO [unsatCount=" + unsatCount + ", marginalCount=" + marginalCount + ", satCount=" + satCount
				+ ", naCount=" + naCount + "]";
	}

}
