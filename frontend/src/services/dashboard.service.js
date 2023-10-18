import axios from "axios";

function getCurrentQuarter(domainId) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/processes/getDashboard`, {
            params: {
                domainId: domainId
            }
        })
        .then(res => {
            return res.data;
        });
}

function getCalculatedRatingCounts(domainId) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/graphs/countCalculatedRatings`, {
            params: {
                domainId: domainId
            }
        })
        .then(res => {
            return res.data;
        });
}

function getFLLRatingCounts(domainId) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/graphs/countFLLratings`, {
            params: {
                domainId: domainId
            }
        })
        .then(res => {
            return res.data;
        });
}

function getProgress(domainId) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/graphs/countProgressGraph`, {
            params: {
                domainId: domainId
            }
        })
        .then(res => {
            return res.data;
        });
}

export const dashboardService = {
    getCurrentQuarter,
    getCalculatedRatingCounts,
    getFLLRatingCounts,
    getProgress
};
