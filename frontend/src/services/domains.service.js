import axios from "axios";

/**
 * returns array of all domains
 */
function getDomains() {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/domains`)
        .then(result => result);
}

/**
 * returns all data for a domain by its ID
 * @param {Int} id domain ID
 */
function getDomain(id) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/domains/${id}`)
        .then(result => {
            return result;
        });
}

/**
 * used to create a domain
 * @param {Object} data
 */
function createDomain(data) {
    return axios
        .post(`${process.env.VUE_APP_API_URL}/domains/add`, data)
        .then(response => response.data.message);
}

/**
 * Used to update a domain
 * @param {Object} domain
 */
function updateDomain(domain) {
    return axios
        .put(`${process.env.VUE_APP_API_URL}/domains/save`, domain)
        .then(response => response.data.message);
}

/**
 * used to calculate domain report from collected squad reports data
 * @param {Object} report required parameters are id - domain id and processAssessments array
 */
function calculateDomainReport(report) {
    return axios
        .put(`${process.env.VUE_APP_API_URL}/domains/reports/save`, report)
        .then(result => result);
}

/**
 * returns most recent domain report by domain Id
 * @param {Int} id
 */
function getDomainReport(id) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/domains/reports/`, {
            params: { domainId: id }
        })
        .then(result => {
            return result;
        });
}

/**
 * used to store domain report data
 * @param {Object} data see swagger
 */
function storeDomainReport(data) {
    return axios
        .put(`${process.env.VUE_APP_API_URL}/domains/reports/save`, data)
        .then(result => result);
}

/**
 * used to retrieve a domain report for speciffic quarter - history view
 * @param {Object} data required are "id" and "quarter"
 */
function getDomainReportByQuarter(data) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/domains/reports/`, {
            params: { ...data }
        })
        .then(result => {
            return result;
        });
}

export const domainsService = {
    getDomains,
    getDomain,
    createDomain,
    updateDomain,
    calculateDomainReport,
    getDomainReport,
    storeDomainReport,
    getDomainReportByQuarter
};
