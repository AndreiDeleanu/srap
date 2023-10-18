import { processesService } from "../services/processes.service";

const state = {
    isLoading: false,
    processes: []
};

const actions = {
    getAll({ commit }, domainId) {
        commit("isLoading", true);

        processesService
            .getAll(domainId)
            .then(processes => commit("getAllSuccess", processes))
            .then(() => commit("isLoading", false));
    },
    createProcess({ commit, dispatch }, data) {
        commit("isLoading", true);

        processesService
            .createProcess(data)
            .then(() => {
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "New Process created!",
                        subtitle: data.name,
                        caption: "",
                        severity: "success"
                    },
                    { root: true }
                );
            })
            .then(() => {
                dispatch("getAll", data.domainId);
            })
            .catch(r => {
                commit("isLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Failed to create process!",
                        subtitle: "",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    updateProcess({ commit, dispatch }, process) {
        commit("isLoading", true);

        processesService
            .updateProcess(process)
            .then(() => {
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Process updated!",
                        subtitle: process.name,
                        caption: "",
                        severity: "success"
                    },
                    { root: true }
                );
            })
            .then(() => {
                dispatch("getAll", process.domainId);
            })
            .catch(r => {
                commit("isLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Failed to update process!",
                        subtitle: process.name,
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    exportToExcel({ commit, dispatch }, data) {
        commit("isLoading", true);

        processesService
            .exportToExcel(data)
            .then(() => {
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Export succes!",
                        subtitle: 'You need to allow window popups for the file to download.',
                        caption: '',
                        severity: "info"
                    },
                    { root: true }
                );
                commit("isLoading", false);
            })
            .catch(r => {
                commit("isLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Export failed!",
                        subtitle: '',
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    }
};

const mutations = {
    isLoading(state, val) {
        state.isLoading = val;
    },
    getAllSuccess(state, processes) {
        state.processes = processes;
    }
};

export const processes = {
    namespaced: true,
    state,
    actions,
    mutations
};
