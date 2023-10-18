package com.ibm.srap.client_beans;

public class ProgressGraphDO {

	private Integer untouchedCount;
	private Integer draftCount;
	private Integer submittedCount;
	private Integer signedCount;

	public Integer getUntouchedCount() {
		return untouchedCount;
	}

	public void setUntouchedCount(Integer untouchedCount) {
		this.untouchedCount = untouchedCount;
	}

	public Integer getDraftCount() {
		return draftCount;
	}

	public void setDraftCount(Integer draftCount) {
		this.draftCount = draftCount;
	}

	public Integer getSubmittedCount() {
		return submittedCount;
	}

	public void setSubmittedCount(Integer submittedCount) {
		this.submittedCount = submittedCount;
	}

	public Integer getSignedCount() {
		return signedCount;
	}

	public void setSignedCount(Integer signedCount) {
		this.signedCount = signedCount;
	}

	@Override
	public String toString() {
		return "ProgressGraphDO [untouchedCount=" + untouchedCount + ", draftCount=" + draftCount + ", submittedCount="
				+ submittedCount + ", signedCount=" + signedCount + "]";
	}

}
