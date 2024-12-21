<template>
  <div class="login-container">
    <div class="login-box">
      <h2>注册新账号</h2>
      <div class="input-group">
        <label for="username">用户名</label>
        <input type="text" id="username" v-model="username" placeholder="请输入用户名" />
      </div>
      <div class="input-group">
        <label for="password">密码</label>
        <input type="password" id="password" v-model="password" placeholder="请输入密码" />
      </div>
      <div class="buttons">
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
const username = ref('')
const password = ref('')

// 注册处理函数
const register = async () => {
  if (!username.value || !password.value) {
    alert('用户名或密码不能为空');
    return;
  }
  const respond = await fetch('http://localhost:8080/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ name: username.value, password: password.value })
  });

  if (respond.ok) {
    const resJson = await respond.json();
    alert('注册成功！您的账号为: ' + resJson.id + '，请牢记您的账号！');
    window.location.href = '/login';
  } else {
    alert('注册失败!请检查您网络!');
  }
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
  display: flex;
  flex-direction: column;
  align-items: center;
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

.register-btn {
  width: 100px;
  padding: 10px;
  font-size: 14px;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: #28a745;
}

.register-btn:hover {
  background-color: #218838;
}
</style>
