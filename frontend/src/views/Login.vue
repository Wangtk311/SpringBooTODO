<template>
  <div class="login-container">
    <div class="login-box">
      <h2>登录SpringBooTODO</h2>
      <div class="input-group">
        <label for="id">账号</label>
        <input type="text" id="id" v-model="id" placeholder="请输入账号" />
      </div>
      <div class="input-group">
        <label for="password">密码</label>
        <input type="password" id="password" v-model="password" placeholder="请输入密码" />
      </div>
      <div class="buttons">
        <button class="login-btn" @click="login">登录</button>
        <button class="register-btn" @click="register">注册</button>
      </div>
    </div>
  </div>
</template>

<script setup>
// 引入 Vuex store
import { useStore } from 'vuex'
import { ref } from 'vue'

// 获取 Vuex store
const store = useStore()

// 定义表单输入字段
const id = ref('')
const password = ref('')

// 登录处理函数
const login = async () => {
  if (!id.value || !password.value) {
    alert('用户名和密码不能为空');
    return;
  }

  // 调用 Vuex 的 login action 进行登录
  await store.dispatch('login', { id: id.value, password: password.value });

  // 登录成功后可以进行跳转或其他操作
  if (store.getters.isAuthenticated) {
    window.location.href = '/';
  } else {
    alert('登录失败!');
  }
}

// 注册处理函数
const register = () => {
  // 跳转到注册页面
  window.location.href = '/register';
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: transparent;
}

.login-box {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  width: 300px;
}

h2 {
  text-align: center;
  font-size: 26px;
  margin-bottom: 20px;
}

.input-group {
  margin-bottom: 15px;
}

.input-group label {
  display: block;
  font-size: 14px;
  color: #555;
  margin-bottom: 5px;
}

.input-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  font-size: 14px;
}

.buttons {
  display: flex;
  justify-content: space-between;
}

.login-btn, .register-btn {
  width: 48%;
  padding: 10px;
  font-size: 14px;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.login-btn {
  background-color: #007bff;
}

.register-btn {
  background-color: #28a745;
}

.login-btn:hover {
  background-color: #0056b3;
}

.register-btn:hover {
  background-color: #218838;
}
</style>
