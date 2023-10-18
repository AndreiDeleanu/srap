package com.ibm.srap.services.utils;

import com.ibm.srap.client_beans.ThresholdDO;

/**
 * @author MihaiMesesan
 *
 *         Ratings calculations class - adjusted rating values are used because
 *         the requirements changed after the application started storing data
 *
 */
public final class RatingValues {

	public static final int MARGINAL_THRESHOLD = 75;
	public static final int SAT_THRESHOLD = 90;

	public static final double MAX_THRESHOLD = 100.0;
	public static final double MIN_THRESHOLD = 0.0;
	public static final double MARG_THRESHOLD = 75.0;
	public static final double SATIS_THRESHOLD = 90.0;

	public static final int DB_SAT = 0;
	public static final int DB_MARGINAL = 1;
	public static final int DB_UNSAT = 2;
	public static final int DB_NA = -1;

	public static final int ADJUSTED_SAT = 3;
	public static final int ADJUSTED_MARGINAL = 2;
	public static final int ADJUSTED_UNSAT = 0;

	private RatingValues() {
	}

	public static double calculatePercentage(Integer totalValue, Integer measurements) {
		int highestScore = measurements * ADJUSTED_SAT;
		return ((double) totalValue / highestScore) * 100;
	}

	public static int averageValues(Integer totalValue, Integer measurements, ThresholdDO thresholdDO) {
		double percentage = calculatePercentage(totalValue, measurements);

		if (percentage >= thresholdDO.getMarginalThreshold() && percentage < thresholdDO.getSatThreshold())
			return DB_MARGINAL;
		if (percentage >= thresholdDO.getSatThreshold())
			return DB_SAT;
		return DB_UNSAT;
	}

}
