import { subdomainReportService } from "../services/subdomain-report.service.js";

const state = {
    isLoading: false,
    subdomainsReports: {},
    subdomainReport: {}
};

const actions = {
    getAll({ commit, dispatch }, domainId) {
        commit("updateLoading", true);

        subdomainReportService
            .getAll(domainId)
            .then(response => {
                commit("setAll", response.data);
                commit("updateLoading", false);
            })
            .catch(response => {
                commit("updateLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not retreive subdomain reports!",
                        subtitle: "",
                        caption: response.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },

    get({ commit, dispatch }, id) {
        commit("updateLoading", true);
        commit("setReport", {});

        subdomainReportService
            .getSubdomainCurrent(id)
            .then(r => {
                commit("setReport", r.data[0]);
                commit("updateLoading", false);
            })
            .catch(r => {
                commit("updateLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not retreive subdomain report!",
                        subtitle: "",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },

    calculate({ commit, dispatch }, data) {
        commit("updateLoading", true);

        subdomainReportService
            .calculateReport(data)
            .then(
                //eslint-disable-next-line
                r => {
                    dispatch("get", data.subdomainId);
                }
            )
            .catch(r => {
                commit("updateLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not perform calculation!",
                        subtitle: "",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    save({ commit, dispatch }, data) {
        commit("updateLoading", true);

        subdomainReportService
            .saveReport(data)
            .then(
                //eslint-disable-next-line
                r => {
                    dispatch("get", data.subdomainId);
                }
            )
            .catch(r => {
                commit("updateLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not save data!",
                        subtitle: "Try again later or contact Admin",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    getByQuarter({ commit, dispatch }, data) {
        commit("updateLoading", true);

        subdomainReportService
            .getByQuarter(data)
            .then(response => {
                commit("setAll", response.data);
                commit("updateLoading", false);
            })
            .catch(response => {
                commit("updateLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not retreive subdomain reports!",
                        subtitle: "",
                        caption: response.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    getSpecificReport({ commit, dispatch }, id) {
        commit("updateLoading", true);

        subdomainReportService
            .getSpecificReport(id)
            .then(r => {
                commit("setReport", r.data);
                commit("updateLoading", false);
            })
            .catch(r => {
                commit("updateLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not retreive subdomain report!",
                        subtitle: "",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },

    stepBack({ commit, dispatch }, data) {
        commit("updateLoading", true);

        subdomainReportService
            .stepBack(data)
            //eslint-disable-next-line
            .then(r => {
                dispatch("get", data.subdomainId);
            })
            .catch(r => {
                commit("updateLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not step back the subdomain report!",
                        subtitle: "",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
};

const mutations = {
    updateLoading(state, loadingState) {
        state.isLoading = loadingState;
    },
    setAll(state, reports) {
        state.subdomainsReports = reports;
    },
    setReport(state, report) {
        const r = report;
        if (r.calculatedValue == null) {
            r.calculatedValue = 0;
        }
        if (r.fllAverage == null) r.fllAverage = 0;
        if (r.sllOverride == null) r.sllOverride = "";
        state.subdomainReport = r;
    }
};

export const subdomainReport = {
    namespaced: true,
    state,
    actions,
    mutations
};
