import axios from "axios";

/**
 * lists all subdomain reports from domain for current quarter
 * used for table overview
 * @param {Int} domainId
 */
function getAll(domainId) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/subdomains/reports/${domainId}`)
        .then(response => {
            return response;
        });
}

/**
 * gets subdomain report for current quarter
 * @param {Int} subdomainId
 */
function getSubdomainCurrent(subdomainId) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/subdomains/reports/current`, {
            params: { subdomainId: subdomainId }
        })
        .then(r => r);
}

/**
 * calculates the rollup for subdomain from collected squads reports data
 * @param {Object} subdomain check swagger
 */
function calculateReport(subdomain) {
    return axios
        .put(
            `${process.env.VUE_APP_API_URL}/subdomains/reports/save`,
            subdomain
        )
        .then(r => r);
}

/**
 * used to save subdomain report
 * @param {Object} subdomain check swagger
 */
function saveReport(subdomain) {
    return axios
        .put(
            `${process.env.VUE_APP_API_URL}/subdomains/reports/save`,
            subdomain
        )
        .then(r => r);
}

/**
 * returns subdomains reports list for speciffic quarter
 * used for history view
 * @param {Object} data check swagger
 */
function getByQuarter(data) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/subdomains/reports/filtered`, {
            params: { ...data }
        })
        .then(response => {
            return response;
        });
}

/**
 * returns subdomain report for speciffic quarter
 * used for history view
 * @param {Int} id
 */
function getSpecificReport(id) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/subdomains/reports/${id}`)
        .then(r => r);
}

function stepBack(data) {
    return axios
        .put(
            `${process.env.VUE_APP_API_URL}/subdomains/reports/status`,
            data
        )
        .then(r => r);
}

export const subdomainReportService = {
    getAll,
    getSubdomainCurrent,
    calculateReport,
    saveReport,
    getByQuarter,
    getSpecificReport,
    stepBack
};
