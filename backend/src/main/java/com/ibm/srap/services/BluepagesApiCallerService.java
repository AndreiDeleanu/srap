package com.ibm.srap.services;

import java.util.List;

import com.ibm.srap.client_beans.SearchResultUserDO;

public interface BluepagesApiCallerService {
	
	List<SearchResultUserDO> findUsers(String input);

}
