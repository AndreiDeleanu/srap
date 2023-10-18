import { deadlinesService } from "../services/deadlines.service";

const state = {
    isLoading: false,
    deadlines: []
};

const actions = {
    getDomainDeadlines({ commit, dispatch }, domainId) {
        commit("setLoadingState", true);

        deadlinesService
            .getDomainDeadlines(domainId)
            .then(r => {
                commit("setDeadlines", r.data);
                commit("setLoadingState", false);
            })
            .catch(r => {
                commit("setLoadingState", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not retreive Deadlines from DB",
                        subtitle: "",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    createDeadline({ commit, dispatch }, deadline) {
        commit("setLoadingState", true);

        deadlinesService
            .createDeadline(deadline)
            //eslint-disable-next-line
            .then(r => {
                dispatch("getDomainDeadlines", deadline.domainId);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Deadline created",
                        subtitle: "",
                        caption: "",
                        severity: "success"
                    },
                    { root: true }
                );
            })
            .catch(r => {
                commit("setLoadingState", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not create Deadline from DB",
                        subtitle: "",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    updateDeadline({ commit, dispatch }, deadline) {
        commit("setLoadingState", true);

        deadlinesService
            .updateDeadline(deadline)
            //eslint-disable-next-line
            .then(r => {
                dispatch("getDomainDeadlines", deadline.domainId);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Deadline updated",
                        subtitle: "",
                        caption: "",
                        severity: "success"
                    },
                    { root: true }
                );
            })
            .catch(r => {
                commit("setLoadingState", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not update Deadline from DB",
                        subtitle: "",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    }
};

const mutations = {
    setLoadingState(state, status) {
        state.isLoading = status;
    },
    setDeadlines(state, data) {
        state.deadlines = data;
    }
};

export const deadlines = {
    namespaced: true,
    state,
    actions,
    mutations
};
