import { loginService } from "../services/login.service";
import router from "../router";
import axios from "axios";

const state = {
    isLoading: false,
    user: {
        delegatedRoles: [],
        delegations: [],
        roles: []
    },
    loginFrom: ""
};

const actions = {
    debugSetRole({ commit }, role) {
        commit("debugSetRole", role);
    },
    login({ commit, dispatch }, loginData) {
        commit("isLoading", true);
        loginService
            .login(loginData.user)
            .then(user => {
                // if unauthorised display error message
                // eslint-disable-next-line
                if (!!user.response) {
                    console.log("got response");
                    commit("isLoading", false);

                    if (user.response.status === 401) {
                        dispatch(
                            "notifications/createNotification",
                            {
                                title: "Not Authorized!",
                                subtitle: "Email or Password is not correct",
                                caption: "please try again",
                                severity: "error"
                            },
                            { root: true }
                        );
                    } else {
                        dispatch(
                            "notifications/createNotification",
                            {
                                title: "Not Authorized!",
                                subtitle: user.response.message,
                                caption: "please try again",
                                severity: "error"
                            },
                            { root: true }
                        );
                    }
                    return;
                }

                const domainId =
                    user.roles.length > 0
                        ? user.domainId
                        : user.delegations.length > 0
                        ? [user.delegations[0].domainId]
                        : -1;
                user.domainId = domainId;

                commit("getSuccess", user);

                // add token to session
                sessionStorage.setItem("token", user.token);

                // add session token to all axios calls
                axios.defaults.headers.common["Authorization"] = `Bearer ${
                    user.token
                }`;

                // set user cookie
                window.$cookies.set("srapusr", user, "1d");

                if (user.domainId === -1) {
                    router.replace("/unauthorised");
                    return;
                }

                // else go to app
                if (loginData.redirect == "/") {
                    router.replace("/" + domainId[0]);
                } else {
                    router.replace(loginData.redirect);
                }
                dispatch("domains/getDomain", domainId[0], { root: true });
            })
            .then(() => commit("isLoading", false))
            .catch(() => commit("isLoading", false));
    },
    previouslyLoged({ commit, dispatch }, userData) {
        commit("isLoading", true);
        commit("getSuccess", userData);
        dispatch("domains/getDomain", userData.domainId[0], { root: true });
        commit("isLoading", false);
    },
    logout({ commit }) {
        commit("getSuccess", {
            delegations: []
        });
    }
};

const mutations = {
    isLoading(state, val) {
        state.isLoading = val;
    },
    getSuccess(state, user) {
        // console.log(user)
        state.user = user;
        state.user.delegatedRoles = user.delegations.map(delegation => {
            return delegation.role;
        });
    }

    // debugSetRole(state, role) {
    //     state.user.role = role;
    // }
};

const getters = {
    getUser(state) {
        return state.user;
    }
};

export const user = {
    namespaced: true,
    state,
    actions,
    mutations,
    getters
};
