import { BPservice } from '../services/BP.service';

const state = {
    isLoading: true,
    users: [],
}

const actions = {
    getSugestions({ commit }, query) {
        commit('loading', true);
        BPservice.getSugestions(query)
            .then((result) => {
                commit('getSugestions', result);
                commit('loading', false);
            }).catch((err) => {
                console.error(err);
            });
    }
}

const mutations = {
    loading(state, status) {
        state.isLoading = status;
    },
    getSugestions(state, results) {
        state.users = results;
    }
}

export const BP = {
    namespaced: true,
    state,
    actions,
    mutations
}
