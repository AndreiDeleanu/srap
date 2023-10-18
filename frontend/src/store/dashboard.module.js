import { dashboardService } from "../services/dashboard.service";

const state = {
    loading: false,
    datagridData: [],
    calculatedLoading: false,
    calculatedRatingCounts: {},
    fllLoading: false,
    fllRatingCounts: {},
    progressLoading: false,
    progressCounts: {}
};

const actions = {
    getCurrentQuarter({ commit, dispatch }, domainId) {
        commit("updateLoadingState", true);

        dashboardService
            .getCurrentQuarter(domainId)
            .then(response => {
                commit("setDatagridData", response);
                commit("updateLoadingState", false);
            })
            .catch(response => {
                commit("updateLoadingState", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not retreive domain process reports!",
                        subtitle: domainId,
                        caption: response.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },

    getCalculatedRatingCounts({ commit, dispatch }, domainId) {
        commit("updateCalculatedLoading", true);

        dashboardService
            .getCalculatedRatingCounts(domainId)
            .then(response => {
                commit("setCalculatedRatings", response);
                commit("updateCalculatedLoading", false);
            })
            .catch(response => {
                commit("updateCalculatedLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title:
                            "Could not retreive counts for calculated ratings!",
                        subtitle: domainId,
                        caption: response.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },

    getFLLRatingCounts({ commit, dispatch }, domainId) {
        commit("updateFllLoading", true);

        dashboardService
            .getFLLRatingCounts(domainId)
            .then(response => {
                commit("setFllRatings", response);
                commit("updateFllLoading", false);
            })
            .catch(response => {
                commit("updateFllLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not retreive counts for fll overrides!",
                        subtitle: domainId,
                        caption: response.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },

    getProgress({ commit, dispatch }, domainId) {
        commit("updateProgressLoading", true);

        dashboardService
            .getProgress(domainId)
            .then(response => {
                commit("setProgressCounts", response);
                commit("updateProgressLoading", false);
            })
            .catch(response => {
                commit("updateProgressLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not retreive progress data!",
                        subtitle: domainId,
                        caption: response.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    }
};

const mutations = {
    updateLoadingState(state, newLoadingState) {
        state.loading = newLoadingState;
    },
    setDatagridData(state, response) {
        state.datagridData = response;
    },
    updateCalculatedLoading(state, newLoadingState) {
        state.calculatedLoading = newLoadingState;
    },
    setCalculatedRatings(state, ratings) {
        state.calculatedRatingCounts = ratings;
    },
    updateFllLoading(state, newLoadingState) {
        state.fllLoading = newLoadingState;
    },
    setFllRatings(state, ratings) {
        state.fllRatingCounts = ratings;
    },
    updateProgressLoading(state, loadingState) {
        state.progressLoading = loadingState;
    },
    setProgressCounts(state, counts) {
        state.progressCounts = counts;
    }
};

export const dashboardModule = {
    namespaced: true,
    state,
    actions,
    mutations
};
