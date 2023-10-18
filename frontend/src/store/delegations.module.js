import { delegationsService } from '../services/delegations.service'

const state = {
    isLoading: false,
    delegations: [],
}

const actions = {
    getDelegations({ commit, dispatch }, user) {
        commit('setLoadingState', true);

        delegationsService.getDelegations(user).then(
            r => {
                commit('setDelegations', r.data);
                commit('setLoadingState', false);
            }
        ).catch(r => {
            dispatch('notifications/createNotification', {
                title: 'Could not retreive Delegations from DB',
                subtitle: '',
                caption: r.message,
                severity: 'error'
            }, { root: true });
            commit('setLoadingState', false);
        });
    },

    addDelegation({ commit, dispatch }, delegation) {
        commit('setLoadingState', true);

        delegationsService.addDelegation(delegation).then(
            r => { // eslint-disable-line
                dispatch('notifications/createNotification', {
                    title: 'Delegation created',
                    subtitle: delegation.delegate,
                    caption: '',
                    severity: 'success'
                }, { root: true });
                dispatch('getDelegations', delegation.delegator);
            }
        ).catch(r => {
            dispatch('notifications/createNotification', {
                title: 'Could not create Delegation',
                subtitle: 'Please try again later!',
                caption: r.message,
                severity: 'error'
            }, { root: true });
            commit('setLoadingState', false);
        });
    },

    updateDelegation({ commit, dispatch }, delegation) {
        commit('setLoadingState', true);

        delegationsService.updateDelegation(delegation).then(
            r => { // eslint-disable-line
                dispatch('notifications/createNotification', {
                    title: 'Delegation updated',
                    subtitle: delegation.delegate,
                    caption: '',
                    severity: 'success'
                }, { root: true });
                dispatch('getDelegations', delegation.delegator);
            }
        ).catch(r => {
            dispatch('notifications/createNotification', {
                title: 'Could not update Delegation',
                subtitle: 'Please try again later!',
                caption: r.message,
                severity: 'error'
            }, { root: true });
            commit('setLoadingState', false);
        });
    },

    deleteDelegation({ commit, dispatch }, data) {
        commit('setLoadingState', true);

        delegationsService.deleteDelegation(data.id).then(
            r => { // eslint-disable-line
                dispatch('notifications/createNotification', {
                    title: 'Delegation deleted',
                    subtitle: '',
                    caption: '',
                    severity: 'success'
                }, { root: true });
                dispatch('getDelegations', data.email);
            }
        ).catch(r => {
            dispatch('notifications/createNotification', {
                title: 'Could not delete Delegation',
                subtitle: 'Please try again later!',
                caption: r.message,
                severity: 'error'
            }, { root: true });
            commit('setLoadingState', false);
        });
    }
}

const mutations = {
    setLoadingState(state, status) {
        state.isLoading = status;
    },
    setDelegations(state, delegations) {
        state.delegations = delegations
    }
}

export const delegations = {
    namespaced: true,
    state,
    actions,
    mutations
}
