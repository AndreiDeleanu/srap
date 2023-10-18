import axios from "axios";

export const PROCESS_STATUS = {
    UNTOUCHED: 0,
    CORRECT: 1,
    NO_RATING: 2,
    NO_COMMENT: 3
};

export const PROCESS_RATING = {
    NA: -1,
    SAT: 0,
    MARGINAL: 1,
    UNSAT: 2
};

/**
 * converts processes assessments ratings from user readable values to DB IDs
 * @param {Array} processes
 */
function transformProcessRatingsToInts(processes) {
    return processes.map(p => {
        p.rating = transfromRatingToInt(p.rating);
        return p;
    });
}

/**
 * converts processes assessments ratings from DB IDs to user readable values
 * @param {Array} processes
 */
function transformProcessRatingsToStrings(processes) {
    return processes.map(p => {
        p.rating = transfromRatingToString(p.rating);
        return p;
    });
}

/**
 * Transforms DB ID for rating to a user readable rating
 * @param {Int} rating
 */
function transfromRatingToString(rating) {
    switch (rating) {
        case PROCESS_RATING.MARGINAL:
            rating = "Marginal";
            break;
        case PROCESS_RATING.NA:
            rating = "n/a";
            break;
        case PROCESS_RATING.SAT:
            rating = "Sat";
            break;
        case PROCESS_RATING.UNSAT:
            rating = "Unsat";
            break;
        default:
            break;
    }
    return rating;
}

/**
 * Transforms a user readable rating to DB ID for rating
 * @param {String} rating
 */
function transfromRatingToInt(rating) {
    switch (rating) {
        case "Marginal":
            rating = PROCESS_RATING.MARGINAL;
            break;
        case "n/a":
            rating = PROCESS_RATING.NA;
            break;
        case "Sat":
            rating = PROCESS_RATING.SAT;
            break;
        case "Unsat":
            rating = PROCESS_RATING.UNSAT;
            break;
        default:
            break;
    }
    return rating;
}

/**
 * returns current quarter squad repory by squad ID
 * @param {Int} squadId
 */
function get(squadId) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/squads/reports/filtered/`, {
            params: { squadId: squadId }
        })
        .then(res => {
            if (
                res.data &&
                (res.data.status === undefined || res.data.status === null)
            ) {
                res.data.status = "Draft"; // set as new if the report exists but it has no status
            }
            res.data.processes = transformProcessRatingsToStrings(
                res.data.processes
            );
            res.data.fllRating = transfromRatingToString(res.data.fllRating);
            return res.data; // FIXME: only returning the first report for now
        });
}

/**
 * used to save report data
 * @param {Object} report check swagger
 */
function save(report) {
    report.fllRating = transfromRatingToInt(report.fllRating);
    report.processes = transformProcessRatingsToInts(report.processes);
    if (report.status == "") {
        report.status = "Draft";
    }
    return axios.put(
        `${process.env.VUE_APP_API_URL}/squads/reports/save`,
        report
    );
}

/**
 * Actually does exactly the same thing as the previous one
 * @param {Object} report
 */
function submit(report) {
    report.fllRating = transfromRatingToInt(report.fllRating);
    report.processes = transformProcessRatingsToInts(report.processes);

    return axios.put(
        `${process.env.VUE_APP_API_URL}/squads/reports/save`,
        report
    );
}

/**
 * updates squad report status to draft
 * @param {Int} reportID
 */
function stepBackToFP(reportID) {
    return axios.put(`${process.env.VUE_APP_API_URL}/squads/reports/status`, {
        reportId: reportID,
        name: "Draft"
    });
}

/**
 * used to move report to a previous step - needs to be specified
 * @param {Object} data
 * @param {Int} data.reportId
 * @param {String} data.step step that you want to move the report to
 */
function stepBack(data) {
    return axios.put(`${process.env.VUE_APP_API_URL}/squads/reports/status`, {
        reportId: data.reportId,
        name: data.step
    });
}

/**
 * returns array of reports for speciffic quarter
 * @param {Object} data check swagger
 */
function getReportsListByQuarter(data) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/squads/reports/`, {
            params: { ...data }
        })
        .then(res => res);
}

/**
 * returns squad report by its id - used for history views
 * @param {Int} id id of squad report
 */
function getSpecificReport(id) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/squads/reports/${id}`)
        .then(res => {
            const report = res.data;
            report.fllRating = transfromRatingToString(report.fllRating);
            report.processes = transformProcessRatingsToStrings(
                report.processes
            );
            return report; // FIXME: only returning the first report for now
        });
}

export const squadReportService = {
    get,
    save,
    submit,
    stepBackToFP,
    getReportsListByQuarter,
    getSpecificReport,
    stepBack
};
