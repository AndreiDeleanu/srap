package com.ibm.srap.services;

import java.util.List;

import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.client_beans.SquadDO;
import com.ibm.srap.client_beans.SquadUpdateDO;

public interface SquadService {
	
	List<SquadDO> getSquadsByDomain(Integer domainId);
	
	boolean createNewSquad(SquadDO sb);
	
	OperationResult updateSquad(SquadUpdateDO updateBean);
	
	SquadDO getSquadForUser(String user);
	
}
