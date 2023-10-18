package com.ibm.srap.services;

import java.util.List;

import com.ibm.srap.client_beans.DeadlineDO;
import com.ibm.srap.client_beans.OperationResult;

public interface DeadlineService {

	List<DeadlineDO> getDeadlines(Integer input);

	OperationResult createDeadline(DeadlineDO input);

	OperationResult updateDeadline(DeadlineDO input);

}
