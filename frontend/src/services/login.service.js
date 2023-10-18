import axios from "axios";

/**
 * used to authenticate user
 * @param {Object} loginData user data
 * @param {String} loginData.password
 * @param {String} loginData.username
 */
function login(loginData) {
    return axios
        .post(`${process.env.VUE_APP_API_URL}/auth/login`, loginData)
        .then(response => {
            return response.data;
        })
        .catch(response => {
            return response;
        });
}

export const loginService = {
    login
};
