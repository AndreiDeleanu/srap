const state = {
    container: null,
    fileName: 'dumy'
};

const actions = {
    update({ commit }, data) {
        commit('update', data);
    }
};

const mutations = {
    update(state, data) {
        state.container = data.container;
        state.fileName = data.filename;
    }
};

export const pdf = {
    namespaced: true,
    state,
    actions,
    mutations
};
