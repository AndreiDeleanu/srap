import axios from "axios";

function getThresholds(domainId) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/thresholds/getAllByDomainId`, {
            params: { domainId: domainId }
        })
        .then(res => res);
}

function getCurrentThreshold(domainId) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/thresholds/getThreshold`, {
            params: { domainId: domainId }
        })
        .then(res => res);
}

function getThresholdByQuarter(data) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/thresholds/getSpecificThreshold`, {
            params: data
        })
        .then(res => res);
}

function addThreshold(threshold) {
    return axios
        .post(`${process.env.VUE_APP_API_URL}/thresholds/add`, threshold)
        .then(res => res);
}

function updateThreshold(threshold) {
    return axios
        .put(`${process.env.VUE_APP_API_URL}/thresholds/update`, threshold)
        .then(res => res);
}

export const thresholdsService = {
    getThresholds,
    getCurrentThreshold,
    getThresholdByQuarter,
    addThreshold,
    updateThreshold
};
