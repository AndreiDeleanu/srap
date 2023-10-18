import axios from "axios";

/**
 * used to call BE to send an email
 * @param {Object} email see swagger
 */
function sendEmail(email) {
    return axios
        .post(`${process.env.VUE_APP_API_URL}/notifications/send`, email)
        .then(r => r);
}

export const emailsService = {
    sendEmail
};
