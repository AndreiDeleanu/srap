const state = {
    isLoading: false
};

const actions = {
    loading({ commit }, status) {
        commit('loading', status);
    }
};

const mutations = {
    loading(state, status) {
        state.isLoading = status;
    }
};

export const spinner = {
    namespaced: true,
    state,
    actions,
    mutations
};
