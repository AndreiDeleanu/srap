package com.ibm.srap.services.implementations;

import static com.ibm.srap.services.utils.SrapUtils.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ibm.srap.client_beans.SearchResultDO;
import com.ibm.srap.client_beans.SearchResultUserDO;
import com.ibm.srap.services.BluepagesApiCallerService;

@Service("BluepagesApiCallerService")
public class BluepagesApiCallerServiceImpl implements BluepagesApiCallerService {


	@Override
	public List<SearchResultUserDO> findUsers(String input) {
		RestTemplate template = new RestTemplate();
		StringBuilder url = new StringBuilder();
		url.append(BP_SEARCH_URL);
		url.append("?"); url.append(BP_SEARCH_CONFIG_PARAM); url.append("="); url.append(BP_SEARCH_CONFIG_VAL);
		url.append("&"); url.append(BP_SEARCH_TIMEOUT_PARAM); url.append("="); url.append(BP_SEARCH_TIMEOUT_VAL);
		url.append("&"); url.append(BP_SEARCH_QUERY_PARAM); url.append("="); url.append(input);
		SearchResultDO result = template.getForObject(url.toString(), SearchResultDO.class);
		return result.getSuggestions();
	}

}
