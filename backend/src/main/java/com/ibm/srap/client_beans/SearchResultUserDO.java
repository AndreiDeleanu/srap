package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.emptyIfNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.ibm.srap.services.utils.SrapUtils;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "fullName", "email", "photo" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResultUserDO {
	
	@JsonProperty(value="nameFull", access=Access.WRITE_ONLY)	
	private String nameIn;
	
	@JsonProperty(value="preferredIdentity", access=Access.WRITE_ONLY)
	private String mailIn;
	
	@JsonProperty(value="fullName", access=Access.READ_WRITE)
	private String fullName;
	
	@JsonProperty(value="email", access=Access.READ_WRITE)
	private String email;
	
	private String photo;

	
	public String getNameIn() { return emptyIfNull(nameIn); }
	public void setNameIn(String n) { nameIn = n; }

	public String getMailIn() { return emptyIfNull(mailIn); }
	public void setMailIn(String m) { mailIn = m; }
	
	public String getFullName() { return getNameIn(); }
	public void setFullName(String n) { fullName = n; }
	
	public String getEmail() { return getMailIn(); }
	public void setEmail(String e) { email = e; }
	
	public String getPhoto() {
		photo = SrapUtils.BLUEPAGES_USER_IMAGE_URL + getMailIn(); 
		return photo.toLowerCase();
	}
	public void setPhoto(String p) { photo = p; }
	
}
