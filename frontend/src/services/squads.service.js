import axios from "axios";

/**
 * returns all current quarter squad reports for table overview
 * should be moved to squad-report.service.js
 * @param {Int} data domainId
 */
function getAll(data) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/squads/reports`, {
            params: { domainId: data }
        })
        .then(res => {
            return res.data.map(row => {
                return {
                    id: row.squadId,
                    name: row.squadName,
                    FP_name: row.fp,
                    FLL_name: row.fll,
                    SLL_name: row.sll,
                    step: row.step,
                    calculated: row.calculatedRating,
                    fllRating: row.fllRating,
                    status: row.squadStatus
                };
            });
        });
}

/**
 * used to store a new squad
 * @param {Object} data squad data
 */
function store(data) {
    return axios
        .post(`${process.env.VUE_APP_API_URL}/squads/add`, data)
        .then(res => res.data);
}

/**
 * used to update squad
 * @param {Object} data check swagger
 */
function updateSquad(data) {
    return axios.put(`${process.env.VUE_APP_API_URL}/squads/update`, data);
}

/**
 * returns array of all squads specified for domain
 * @param {Int} domainId
 */
function getAllSquads(domainId) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/squads/?domain=${domainId}`)
        .then(res => {
            return res;
        });
}

export const squadsService = {
    getAll,
    store,
    updateSquad,
    getAllSquads
};
