import axios from "axios";

/**
 * Returns all specified deadlines for a domain
 * @param {Int} domainId
 */
function getDomainDeadlines(domainId) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/deadlines`, {
            params: { domainId: domainId }
        })
        .then(r => r);
}

/**
 * Used to create a deadline for domain
 * @param {Object} deadline
 */
function createDeadline(deadline) {
    return axios
        .post(`${process.env.VUE_APP_API_URL}/deadlines/add`, deadline)
        .then(r => r);
}

/**
 * Used to update deadline for domain
 * @param {Object} deadline
 */
function updateDeadline(deadline) {
    return axios
        .put(`${process.env.VUE_APP_API_URL}/deadlines/update`, deadline)
        .then(r => r);
}

export const deadlinesService = {
    getDomainDeadlines,
    createDeadline,
    updateDeadline
};
