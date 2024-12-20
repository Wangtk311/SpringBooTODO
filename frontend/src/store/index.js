// store/index.js
import { createStore } from 'vuex';

const store = createStore({
    state: {
        userId: null,
        token: null,
    },
    mutations: {
        // 设置用户信息和 token
        setUser(state, { userId, token }) {
            state.userId = userId;
            state.token = token;
            localStorage.setItem('user-id', userId);  // 保存到 localStorage
            localStorage.setItem('jwt-token', token);  // 保存到 localStorage
        },
        // 清除用户信息和 token
        clearUser(state) {
            state.userId = null;
            state.token = null;
            localStorage.removeItem('user-id');  // 从 localStorage 移除
            localStorage.removeItem('jwt-token');  // 从 localStorage 移除
        }
    },
    actions: {
        // 登录操作
        async login({ commit }, { id, password }) {
            const response = await fetch('http://localhost:8080/user/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ id, password }),
            });

            if (response.ok) {
                const data = await response.text()
                const { userId, token } = { userId: id, token: data };

                // 将 userId 和 token 保存到 Vuex 和 localStorage
                commit('setUser', { userId, token });
            } else {
                console.error('登录失败，服务器认证未通过。');
            }
        },
        // 登出操作
        logout({ commit }) {
            commit('clearUser');
        }
    },
    getters: {
        // 判断用户是否已登录
        isAuthenticated: (state) => !!state.token,
        getUserId: (state) => state.userId,
        getToken: (state) => state.token,
    }
});

export default store;
