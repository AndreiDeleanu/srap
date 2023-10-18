import { domainsService } from "../services/domains.service";

const state = {
    isLoading: true,
    domains: [],
    domainReport: {},
    noReport: false,
    domain: {
        help: ""
    }
};

const actions = {
    createDomain({ commit, dispatch }, domain) {
        commit("isLoading", true);

        domainsService.createDomain(domain).then(response => {
            dispatch(
                "notifications/createNotification",
                {
                    title: "Domain created",
                    subtitle: domain.name,
                    caption: response,
                    severity: "success"
                },
                { root: true }
            );
            dispatch("getAll");
        });
        // .catch(e => {
        //     commit('isLoading', false);
        //     dispatch('notifications/createNotification', {
        //         title: 'Failed to create Domain',
        //         subtitle: domain.name,
        //         caption: e.message,
        //         severity: 'error'
        //     }, { root: true });
        // });
    },
    updateDomain({ commit, dispatch }, domain) {
        commit("isLoading", true);

        domainsService.updateDomain(domain).then(response => {
            dispatch(
                "notifications/createNotification",
                {
                    title: "Domain data updated!",
                    subtitle: domain.name,
                    caption: response,
                    severity: "success"
                },
                { root: true }
            );
            dispatch("getAll");
        });
    },
    getDomain({ commit, dispatch }, id) {
        commit("isLoading", true);

        domainsService
            .getDomain(id)
            .then(response => {
                commit("getSuccess", response.data);
            })
            .catch(error => {
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not retreive from DB",
                        subtitle: "",
                        caption: error.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    getAll({ commit, dispatch }) {
        commit("isLoading", true);

        domainsService
            .getDomains()
            .then(r => {
                commit("storeDomains", r.data);
                commit("isLoading", false);
            })
            .catch(r => {
                commit("isLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Failed to fetch Domains",
                        subtitle: "",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    getDomainReport({ commit, dispatch }, id = 1) {
        commit("isLoading", true);

        domainsService
            .getDomainReport(id)
            .then(r => {
                commit("setDomainReport", r.data);
                commit("isLoading", false);
            })
            .catch(r => {
                commit("isLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Failed to fetch Domain Report",
                        subtitle: "please try again later",
                        caption: r.response.data.message,
                        severity: "warning"
                    },
                    { root: true }
                );
                commit("setDomainReport", r.response.data);
            });
    },
    getDomainReportByQuarter({ commit, dispatch }, data) {
        commit("isLoading", true);

        domainsService
            .getDomainReportByQuarter(data)
            .then(r => {
                commit("setDomainReport", r.data);
                commit("isLoading", false);
            })
            .catch(r => {
                commit("isLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Failed to fetch Domain Report",
                        subtitle: "please try again later",
                        caption: r.response.data.message,
                        severity: "warning"
                    },
                    { root: true }
                );
                commit("setDomainReport", r.response.data);
            });
    },
    calculateDomainReport({ commit, dispatch }, data) {
        commit("isLoading", true);

        domainsService
            .calculateDomainReport(data)
            .then(() => {
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Gathered data for Domain Roll-Up",
                        subtitle: "",
                        caption: "",
                        severity: "info"
                    },
                    { root: true }
                );
                dispatch("getDomainReport", data.id);
            })
            .catch(r => {
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Failed calculation on Domain Roll-Up",
                        subtitle: "please try again later",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
                commit("isLoading", false);
            });
    },
    saveDomainReport({ commit, dispatch }, data) {
        commit("isLoading", true);

        domainsService
            .storeDomainReport(data)
            .then(() => {
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Domain Roll-Up saved",
                        subtitle: "",
                        caption: "",
                        severity: "success"
                    },
                    { root: true }
                );
                commit("isLoading", false);
            })
            .catch(r => {
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Failed to save Domain Roll-Up",
                        subtitle: "please try again later",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
                commit("isLoading", false);
            });
    }
};

const mutations = {
    isLoading(state, val) {
        state.isLoading = val;
    },
    getSuccess(state, data) {
        state.domain = data;
    },
    storeDomains(state, domains) {
        state.domains = domains;
    },
    setDomainReport(state, report) {
        if (report.length == 0) {
            state.noReport = true;
            state.domainReport = {
                doAssessment: ""
            };
        } else {
            state.domainReport = report[0];
        }
    }
};

export const domains = {
    namespaced: true,
    state,
    actions,
    mutations
};
