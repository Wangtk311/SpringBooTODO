import { createStore } from 'vuex';

const store = createStore({
    state: {
        userId: localStorage.getItem('user-id') || null,
        token: localStorage.getItem('jwt-token') || null,
        isTokenValid: localStorage.getItem('isTokenValid') || null, // 新增状态字段，用于保存 token 是否有效
        userName: localStorage.getItem('user-name') || null,
    },
    mutations: {
        // 设置用户信息和 token
        setUser(state, { userId, token, userName }) {
            state.userId = userId;
            state.token = token;
            state.isTokenValid = null; // 每次设置 token 时重置有效性验证
            state.userName = userName;
            localStorage.setItem('user-id', userId);  // 保存到 localStorage
            localStorage.setItem('jwt-token', token);  // 保存到 localStorage
            localStorage.setItem('isTokenValid', null); // 保存到 localStorage
            localStorage.setItem('user-name', userName);  // 保存到 localStorage
        },
        // 清除用户信息和 token
        clearUser(state) {
            state.userId = null;
            state.token = null;
            state.isTokenValid = null; // 清除验证结果
            state.userName = null;
            localStorage.removeItem('user-id');  // 从 localStorage 移除
            localStorage.removeItem('jwt-token');  // 从 localStorage 移除
            localStorage.removeItem('isTokenValid'); // 从 localStorage 移除
            localStorage.removeItem('user-name');  // 从 localStorage 移除
        },
        // 设置 token 是否有效
        setTokenValidity(state, isValid) {
            state.isTokenValid = isValid;
            localStorage.setItem('isTokenValid', isValid);  // 保存到 localStorage
        },
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
                const data = await response.json();
                const usertoken = data.token;
                const username = data.username;
                const { userId, token, userName } = { userId: id, token: usertoken, userName: username };

                // 将 userId 和 token 保存到 Vuex 和 localStorage
                commit('setUser', { userId, token, userName });
                this.state.isTokenValid = true;
            } else {
                console.error('登录失败，服务器认证未通过。');
                commit('clearUser');
            }
        },

        // 验证 token 是否有效
        async verifyToken({ state, commit }) {
            if (!state.token) {
                commit('setTokenValidity', false); // 如果没有 token，认为无效
                return;
            }

            try {
                const response = await fetch('http://localhost:8080/user/verifyToken', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${state.token}`,  // 使用 token 进行鉴权
                    },
                });

                if (response.ok) {
                    commit('setTokenValidity', true);  // 根据响应结果设置 token 是否有效
                } else {
                    commit('setTokenValidity', false); // 如果验证失败，则 token 无效
                }
            } catch (error) {
                console.error('验证 token 时发生错误:', error);
                commit('setTokenValidity', false); // 网络错误时也认为 token 无效
            }
        },

        // 登出操作
        logout({ commit }) {
            commit('clearUser');
        },
    },
    getters: {
        // 判断 token 是否有效
        isAuthorized: (state) => state.isTokenValid,
        getUserId: (state) => state.userId,
        getToken: (state) => state.token,
    },
});

export default store;
