package com.ibm.srap.client_beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResultDO {
	
	@JsonProperty("results")
	private List<SearchResultUserDO> suggestions;
	
	
	public List<SearchResultUserDO> getSuggestions() {
		return suggestions;
	}
	public void setSuggestions(List<SearchResultUserDO> suggestions) {
		this.suggestions = suggestions;
	}
	
}
