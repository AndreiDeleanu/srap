import { subdomainsService } from "../services/subdomains.service";

const state = {
    isLoading: false,
    subdomains: []
};

const actions = {
    getAll({ commit, dispatch }, domainId) {
        commit('isLoading', true);

        subdomainsService.getSubdomains(domainId)
            .then(response => commit('getAllSuccess', response.data))
            .then(() => commit('isLoading', false))
            .catch(response => {
                commit('isLoading', false);
                dispatch('notifications/createNotification', {
                    title: 'Could not retreive subdomains!',
                    subtitle: '',
                    caption: response.message,
                    severity: 'error'
                }, { root: true });
            });
    },
    createSubdomain({ commit, dispatch }, subdomain) {
        commit('isLoading', true);

        subdomainsService.createSubdomain(subdomain)
            .then(response => {
                dispatch('notifications/createNotification', {
                    title: 'Subdomain created!',
                    subtitle: subdomain.name,
                    caption: response.message,
                    severity: 'success'
                }, { root: true });
                dispatch('getAll', subdomain.domainId);
            })
            .catch(response => {
                commit('isLoading', false);
                dispatch('notifications/createNotification', {
                    title: 'Subdomain creation failed!',
                    subtitle: subdomain.name,
                    caption: response.message,
                    severity: 'error'
                }, { root: true });
            });
    },
    updateDomain({ commit, dispatch }, subdomain) {
        commit('isLoading', true);

        subdomainsService.updateSubdomain(subdomain)
            .then(response => {
                dispatch('notifications/createNotification', {
                    title: 'Subdomain update!',
                    subtitle: subdomain.name,
                    caption: response.message,
                    severity: 'success'
                }, { root: true });
                dispatch('getAll', subdomain.domainId);
            })
            .catch(response => {
                commit('isLoading', false);
                dispatch('notifications/createNotification', {
                    title: 'Subdomain update failed!',
                    subtitle: subdomain.name,
                    caption: response.message,
                    severity: 'error'
                }, { root: true });
            });
    }
};

const mutations = {
    isLoading(state, val) {
        state.isLoading = val;
    },
    getAllSuccess(state, subdomains) {
        state.subdomains = subdomains;
    }
};

export const subdomains = {
    namespaced: true,
    state,
    actions,
    mutations
};
