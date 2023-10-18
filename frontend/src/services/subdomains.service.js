import axios from "axios";

/**
 * returns list of subdomains from domain
 * @param {Int} domainId
 */
function getSubdomains(domainId) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/subdomains`, {
            params: { domain: domainId }
        })
        .then(res => res);
}

/**
 * used to create a new subdomain
 * @param {Object} subdomain check swagger
 */
function createSubdomain(subdomain) {
    return axios
        .post(`${process.env.VUE_APP_API_URL}/subdomains/add`, subdomain)
        .then(res => res);
}

/**
 * used to update subdomain
 * @param {Object} subdomain check swagger
 */
function updateSubdomain(subdomain) {
    return axios
        .put(`${process.env.VUE_APP_API_URL}/subdomains/save`, subdomain)
        .then(res => res);
}

export const subdomainsService = {
    getSubdomains,
    createSubdomain,
    updateSubdomain
    // getSubdomainById
};
