import { thresholdsService } from "../services/threshold.service";

const state = {
    isLoading: false,
    thresholds: [],
    currentThreshold: {}
};

const actions = {
    getAllThresholds({ commit, dispatch }, domainId) {
        commit("setLoadingState", true);

        thresholdsService.getThresholds(domainId).then(result => {
            // console.log("result :", result);
            commit('setTresholds', result.data);
            commit("setLoadingState", false);
        }).catch(error => {
            dispatch("notifications/createNotification",
                {
                    title: "Could not retrieve thresholds",
                    subtitle: error.response.message,
                    caption: "",
                    severity: "error"
                },
                { root: true })
        });
    },
    getCurrentThreshold({ commit, dispatch }, domainId) {
        commit("setLoadingState", true);

        thresholdsService
            .getCurrentThreshold(domainId)
            .then(result => {
                commit("setCurrentThreshold", result.data);
                commit("setLoadingState", false);
            })
            .catch(error => {
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not retrieve threshold",
                        subtitle: error.response.message,
                        caption: "",
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    getThresholdsByQuarter({ commit, dispatch }, data) {
        commit("setLoadingState", true);

        thresholdsService
            .getThresholdByQuarter(data)
            .then(result => {
                commit("setCurrentThreshold", result.data);
                commit("setLoadingState", false);
            })
            .catch(error => {
                dispatch(
                    "notifications/createNotification",
                    {
                        title: `Could not retrieve threshold for ${data.quarter}Q${data.year}`,
                        subtitle: error.response.message,
                        caption: "",
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    addThreshold({ commit, dispatch }, threshold) {
        commit("setLoadingState", true);

        thresholdsService
            .addThreshold(threshold)
            .then(() => {
                dispatch("getAllThresholds", threshold.domainId)
            })
            .catch(error => {
                dispatch(
                    "notifications/createNotification",
                    {
                        title: `Could not set threshold`,
                        subtitle: error.response.message,
                        caption: "",
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    updateThreshold({ commit, dispatch }, threshold) {
        commit("setLoadingState", true);

        thresholdsService
            .updateThreshold(threshold)
            .then(() => {
                dispatch("getAllThresholds", threshold.domainId)
            })
            .catch(error => {
                dispatch(
                    "notifications/createNotification",
                    {
                        title: `Could not update threshold`,
                        subtitle: error.response.message,
                        caption: "",
                        severity: "error"
                    },
                    { root: true }
                );
            });
    }
};

const mutations = {
    setLoadingState(state, loading) {
        state.isLoading = loading;
    },
    setTresholds(state, allThresholds) {
        state.thresholds = allThresholds;
    },
    setCurrentThreshold(state, threshold) {
        state.currentThreshold = threshold;
    }
};

const getters = {};

export const thresholdsModule = {
    namespaced: true,
    state,
    actions,
    mutations,
    getters
};
