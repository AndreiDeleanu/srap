import { squadsService } from "../services/squads.service";

const state = {
    isLoading: false,
    squads: []
};

const actions = {
    getAll({ commit }, domain) {
        commit("isLoading", true);
        commit("getAllSuccess", []);
        squadsService
            .getAll(domain)
            .then(squads => {
                commit("getAllSuccess", squads);
            })
            .then(() => commit("isLoading", false));
    },
    store({ commit, dispatch }, data) {
        commit("isLoading", true);
        squadsService
            .store(data)
            // eslint-disable-next-line
            .then(response => {
                dispatch("getAllSquads", data.domainId);
            })
            .catch(r => {
                commit("isLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not save new squad!",
                        subtitle: "",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    updateSquad({ commit, dispatch }, data) {
        // eslint-disable-line
        commit("isLoading", true);
        squadsService
            .updateSquad(data)
            // eslint-disable-next-line
            .then(response => {
                dispatch("getAllSquads", data.domainId);
            })
            .catch(r => {
                commit("isLoading", false);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Could not save squad!",
                        subtitle: "",
                        caption: r.message,
                        severity: "error"
                    },
                    { root: true }
                );
            });
    },
    getAllSquads({ commit }, domainId) {
        commit("isLoading", true);
        commit("getAllSuccess", []);

        squadsService
            .getAllSquads(domainId)
            .then(squads => {
                commit("getAllSuccess", squads.data);
            })
            .then(() => commit("isLoading", false));
    }
};

const mutations = {
    isLoading(state, val) {
        state.isLoading = val;
    },
    getAllSuccess(state, squads) {
        state.squads = squads;
    }
};

const getters = {
    squadById: state => id => {
        // I'm pretty squad IDs are unique, so let's search through all stored squads
        let result;
        result = state.squads.find(squad => squad.id === +id);
        if (result === undefined) {
            result = state.squads.find(squad => squad.id === +id);
        }
        return result;
    }
};

export const squads = {
    namespaced: true,
    state,
    actions,
    mutations,
    getters
};
