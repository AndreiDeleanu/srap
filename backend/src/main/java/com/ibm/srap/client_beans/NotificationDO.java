package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

import java.util.ArrayList;
import java.util.List;

public class NotificationDO {
	
	private String contact;
	private List<String> recipients;
	private List<String> cc;
	private List<String> bcc;
	private String subject;
	private String message;
	
	
	public String getContact() { return emptyIfNull(contact); }
	public void setContact(String c) { contact = c; }
	
	public List<String> getRecipients() { 
		if (recipients == null) recipients = new ArrayList<>();
		return recipients; }
	public void setRecipients(List<String> r) { recipients = r; }
	
	public List<String> getCc() {
		if (cc == null) cc = new ArrayList<>();
		return cc;
	}
	public void setCc(List<String> c) { cc = c; }
	
	public List<String> getBcc() {
		if (bcc == null) bcc = new ArrayList<>();
		return bcc;
	}
	public void setBcc(List<String> b) { bcc = b; }
	
	public String getSubject() { return emptyIfNull(subject); }
	public void setSubject(String s) { subject = s; }
	
	public String getMessage() { return emptyIfNull(message); }
	public void setMessage(String m) { message = m; }

}
