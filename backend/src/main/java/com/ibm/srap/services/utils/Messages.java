package com.ibm.srap.services.utils;

/**
 * @author MihaiMesesan
 *
 *	String constants class for various service messages
 */
public final class Messages {
	
	public static final String BAD_REQUEST_INPUT = "Invalid request input";
	
	
	public static final String NO_DOMAIN_ID = "No domain id provided";
	public static final String NO_NAME = "No name provided";
	public static final String NO_SUBDOMAIN_ID = "No subdomain id provided";
	
	public static final String ALREADY_EXISTS = "Entity already exists";
	
	public static final String DOMAIN_NOT_FOUND = "Domain not found";
	public static final String SUBDOMAIN_NOT_FOUND = "Subdomain not found";
	
	public static final String ENTITY_NOT_SAVED = "Entity not saved";
	public static final String ENTITY_SAVED = "Entity saved";
	
	public static final String SYSTEM = "SRAP_SYSTEM";
	public static final String LOGGED_IN_USER = "logged_in";
	
	public static final String JSON = "application/json";
	public static final String EXCEL = "application/vnd.ms-excel";
	
	public static final String NOTIFICATION_SENDER = "noreplySRAP.portal@ibm.com";
	public static final String ADMIN_NOTIFICATION_SUBJECT = "No deadlines exist for current quarter";
	public static final String ADMIN_NOTIFICATION_MESSAGE = "Create a new deadline for the current quarter";
	
	public static final String SRAP_PORTAL_SCHEDULED = "SRAP Portal scheduled task";
	
	public static final String FP_NOTIFICATION_MESSAGE = "Dear Security Focal point your MSAC assessment for current quarter "
			+ "is not ready in SRAP portal, please fill in the data and submit. ";
	public static final String FLL_NOTIFICATION_MESSAGE = "Dear FLL your MSAC assessment for current quarter is not Signed-off in SRAP portal, "
			+ "please fill in overall assessment and submit. ";
	public static final String SLL_NOTIFICATION_MESSAGE = "Dear SLL, the assessment for this quarter MSAC of your squads is not complete. "
			+ "Please remind respective FLLs to Signe-off their squads. ";
	public static final String X_DAYS_REMAINING = "You have %d more days till deadline - 1:00AM GMT";
	
	public static final String ACTION_REQUIRED_SUBJECT = "Your action is required at SRAP portal";
	
	public static final String TESTING_SUBJECT = "SRAP portal automated notification test";
	public static final String TESTING_MESSAGE = "Please Ignore this message it has been sent as a test of "
			+ "automated notification from SRAP portal";

	private Messages() {}
	
}
