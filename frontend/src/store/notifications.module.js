const state = {
    notifications: [
        /* {
            title: String,
            subtitle: String,
            caption: Strgin,
            severity: of [error, info, warning, success]
        } */
    ]
};

const actions = {
    /**
     *
     * @param {*} param0 passed by vue
     * @param {Object} data {title: String, subtitle: String, caption: String, severity: ['success', 'error', 'info', 'warning']}
     */
    createNotification({ commit }, data) {
        commit('createNotification', data);
    },

    /**
     *
     * @param {*} param0 passed by vue
     * @param {Int} index index of notification to be removed
     */
    removeNotification({ commit }, index) {
        commit('removeNotification', index);
    }
};

const mutations = {
    createNotification(state, data) {
        const time = new Date().getTime();
        data.time = time;
        state.notifications.unshift(data);

        setTimeout(() => {
            const indexToRemove = state.notifications.findIndex(x => x.time === time);
            state.notifications.splice(indexToRemove, 1);
        }, 5000)
    },

    removeNotification(state, index) {
        state.notifications.splice(index, 1);
    }
};

export const notifications = {
    namespaced: true,
    state,
    actions,
    mutations
};
