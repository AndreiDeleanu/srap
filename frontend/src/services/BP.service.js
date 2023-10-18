import axios from "axios";

/**
 * Used to retrieve bluepage users based on querry
 * @param {String} query search querry
 */
function getSugestions(query) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/search`, {
            params: { user: query }
        })
        .then(result => result.data.slice(0, 10));
}

export const BPservice = {
    getSugestions
};
