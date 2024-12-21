import { createStore } from 'vuex';
import { serverURL } from '../serverURLConfig.js';

const store = createStore({
    state: {
        userId: localStorage.getItem('user-id') || null,
        token: localStorage.getItem('jwt-token') || null,
        isTokenValid: localStorage.getItem('isTokenValid') || null,
        userName: localStorage.getItem('user-name') || null,
    },
    mutations: {
        setUser(state, { userId, token, userName }) {
            state.userId = userId;
            state.token = token;
            state.isTokenValid = null;
            state.userName = userName;
            localStorage.setItem('user-id', userId);
            localStorage.setItem('jwt-token', token);
            localStorage.setItem('isTokenValid', null);
            localStorage.setItem('user-name', userName);
        },
        clearUser(state) {
            state.userId = null;
            state.token = null;
            state.isTokenValid = null;
            state.userName = null;
            localStorage.removeItem('user-id');
            localStorage.removeItem('jwt-token');
            localStorage.removeItem('isTokenValid');
            localStorage.removeItem('user-name');
        },
        setTokenValidity(state, isValid) {
            state.isTokenValid = isValid;
            localStorage.setItem('isTokenValid', isValid);
        },
    },
    actions: {
        async login({ commit }, { id, password }) {
            const response = await fetch(serverURL + '/user/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ id, password }),
            });

            if (response.ok) {
                const data = await response.json();
                const usertoken = data.token;
                const username = data.username;
                const { userId, token, userName } = { userId: id, token: usertoken, userName: username };

                commit('setUser', { userId, token, userName });
                this.state.isTokenValid = true;
            } else {
                console.error('登录失败，服务器认证未通过。');
                commit('clearUser');
            }
        },

        async verifyToken({ state, commit }) {
            if (!state.token) {
                commit('setTokenValidity', false);
                return;
            }

            try {
                const response = await fetch(serverURL + '/user/tokencheck', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${state.token}`,
                    },
                });

                if (response.ok) {
                    commit('setTokenValidity', true);
                } else {
                    commit('setTokenValidity', false);
                }
            } catch (error) {
                console.error('验证 token 时发生错误:', error);
                commit('setTokenValidity', false);
            }
        },

        logout({ commit }) {
            commit('clearUser');
        },
    },
    getters: {
        isAuthorized: (state) => state.isTokenValid,
        getUserId: (state) => state.userId,
        getToken: (state) => state.token,
    },
});

export default store;
