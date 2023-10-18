package com.ibm.srap.services.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.IsoFields;

import org.springframework.http.ResponseEntity;

import com.ibm.srap.client_beans.DefaultResponse;
import com.ibm.srap.client_beans.OperationResult;

/**
 * @author MihaiMesesan
 * 
 * Utility class for centralizing repeating operations/calls
 */
public class SrapUtils {
	
	public static final String BLUEPAGES_USER_IMAGE_URL = "https://unified-profile.w3ibm.mybluemix.net/myw3/unified-profile-photo/v1/image/";
	public static final String BP_SEARCH_URL = "https://unified-profile.w3ibm.mybluemix.net/myw3/unified-profile/v1/search/user";
	public static final String BP_SEARCH_CONFIG_PARAM = "searchConfig";
	public static final String BP_SEARCH_CONFIG_VAL = "optimized_search";
	public static final String BP_SEARCH_ROWS_PARAM = "rows";
	public static final String BP_SEARCH_ROWS_VAL = "24";
	public static final String BP_SEARCH_TIMEOUT_PARAM = "timeout";
	public static final String BP_SEARCH_TIMEOUT_VAL = "20000";
	public static final String BP_SEARCH_QUERY_PARAM = "query";
	
	
	private SrapUtils() {} 
	
	public static LocalDate getDate() { return LocalDate.now(); }
	
	public static String currentQuarter() {	return String.valueOf(YearMonth.from(getDate()).get(IsoFields.QUARTER_OF_YEAR)); }
	
	public static String currentYear() { return String.valueOf(getDate().getYear()); }
	
	public static String emptyIfNull(String input) {
		if (input == null) return "";
		return input;
	}
	
	public static Integer zeroIfNull(Integer input) {
		if (input == null) input = 0;
		return input;
	}
	
	public static Double zeroIfNull(Double input) {
		if (input == null) input = 0d;
		return input;
	}
	
	public static Timestamp zeroIfNull(Timestamp input) {
		if (input == null) input = new Timestamp(0);
		return input;
	}
	
	public static ResponseEntity<DefaultResponse> evaluateOperationResult(OperationResult operation) {
		if (operation.isSuccessful()) {
			return ResponseEntity.ok(new DefaultResponse(operation.getMessage()));
		} else {
			return ResponseEntity.badRequest().body(new DefaultResponse(operation.getMessage()));
		}
	}

	public static boolean hasData(String input) {
		return (input != null && !input.isEmpty());
	}
	
	public static boolean hasData(Double input) {
		return (input != null);
	}
	
	public static boolean hasData(Integer input) {
		return (input != null);
	}
}
