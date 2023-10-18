import axios from "axios";

/**
 * returns all delegations based on user as delegator
 * @param {String} user user email
 */
function getDelegations(user) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/delegations`, {
            params: { email: user }
        })
        .then(r => r);
}

/**
 * used to create a delegation
 * @param {Object} dalegation
 */
function addDelegation(dalegation) {
    return axios
        .post(`${process.env.VUE_APP_API_URL}/delegations/add`, dalegation)
        .then(r => r);
}

/**
 * used to remove a delegation by its ID
 * @param {Int} id delegation ID
 */
function deleteDelegation(id) {
    return axios
        .delete(`${process.env.VUE_APP_API_URL}/delegations/delete`, {
            data: { id: id }
        })
        .then(r => r);
}

/**
 * used to update a delegation
 * @param {Object} dalegation
 */
function updateDelegation(dalegation) {
    return axios
        .put(`${process.env.VUE_APP_API_URL}/delegations/update`, dalegation)
        .then(r => r);
}

export const delegationsService = {
    getDelegations,
    addDelegation,
    deleteDelegation,
    updateDelegation
};
