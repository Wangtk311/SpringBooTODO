<template>
  <main>
    <Navbar />
    <div class="my-5">
      <div class="mx-auto" style="max-width: 500px; width: 100%;">
        <h1 class="text-center mb-4">编辑待办</h1>
        <form @submit.prevent="updateTodo">
          <div class="row">
            <div class="col-md-12 form-group mb-3">
              <label for="title" class="form-label" style="font-weight: bold;">待办标题</label>
              <input type="text" name="title" id="title" class="form-control" placeholder="待办标题" required v-model="todo.title"/>
              <span class="text-danger" v-if="errors.title">{{ errors.title }}</span>
            </div>
          </div>

          <div class="row">
            <div class="col-md-12 form-group mb-3">
              <label for="description" class="form-label" style="font-weight: bold;">详细内容</label>
              <input type="text" name="description" id="description" class="form-control" placeholder="待办内容" required v-model="todo.description"/>
              <span class="text-danger" v-if="errors.description">{{ errors.description }}</span>
            </div>
          </div>

          <div class="row">
            <div class="col-md-12 form-group mb-3">
              <label for="date" class="form-label" style="font-weight: bold;">截止日期</label>
              <input type="date" name="date" id="date" class="form-control" required v-model="todo.date"/>
              <span class="text-danger" v-if="errors.date">{{ errors.date }}</span>
            </div>
          </div>

          <div class="row">
            <div class="col-md-12 form-group mb-3">
              <label for="priority" class="form-label" style="font-weight: bold;">优先级</label>
              <select name="priority" id="priority" class="form-control" v-model="todo.priority">
                <option value="低">低</option>
                <option value="中">中</option>
                <option value="高">高</option>
              </select>
              <span class="text-danger" v-if="errors.priority">{{ errors.priority }}</span>
            </div>
          </div>

          <div class="row">
            <div class="col-md-12 form-group mb-3">
              <label for="completed" class="form-label" style="font-weight: bold;">标记为已完成</label>
              <div class="checkbox-container">
                <input type="checkbox" class="large-checkbox" id="completed" v-model="todo.completed"/>
              </div>
            </div>
          </div>

          <div class="form-group">
            <button type="submit" class="btn btn-primary w-100">确认</button>
          </div>

          <div v-if="successMessage" class="alert alert-success">
            {{ successMessage }}
          </div>
          <div v-if="errorMessage" class="alert alert-danger">
            {{ errorMessage }}
          </div>
        </form>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Navbar from '../components/Navbar.vue';
import '../assets/styles.css';
import store from '../store/index';
import { serverURL } from '../serverURLConfig.js';

const route = useRoute();
const router = useRouter();

const todo = ref<Record<string, any>>({
  id: '',
  title: '',
  description: '',
  date: '',
  priority: '',
  completed: false
});

const errors = ref<Record<string, string>>({});
const successMessage = ref<string>('');
const errorMessage = ref<string>('');

const getTodo = async () => {
  try {
    await store.dispatch('verifyToken');
    if (!store.state.isTokenValid) {
      alert('登录状态已超时，请重新登录！');
      await store.dispatch('logout');
      await router.push('/login');
      return;
    }
    const url = new URL(serverURL + `/todo/${route.params.id}`);
    const param = { userid: localStorage.getItem('user-id') };
    Object.keys(param).forEach(key => url.searchParams.append(key, param[key]));
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('jwt-token'),
        'Content-Type': 'form-data'
      }
    });
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    const data = await response.json();
    todo.value = data;
    console.log(todo.value);
  } catch (error) {
    console.error('获取待办失败:', error);
    errorMessage.value = '获取待办失败，请检查网络!';
  }
};

const validateForm = () => {
  errors.value = {};
  let isValid = true;

  if (!todo.value.title.trim()) {
    errors.value.title = '必须提供一个标题';
    isValid = false;
  } else if (todo.value.title.length < 2) {
    errors.value.title = '标题不应少于两个字符';
    isValid = false;
  }

  if (!todo.value.description.trim()) {
    errors.value.description = '必须提供一段内容';
    isValid = false;
  } else if (todo.value.description.length < 2) {
    errors.value.description = '内容不应少于两个字符';
    isValid = false;
  }

  if (!todo.value.date) {
    errors.value.date = '必须提供一个截止日期';
    isValid = false;
  } else if (new Date(todo.value.date) <= new Date()) {
    errors.value.date = '不应选择过去的时间';
    isValid = false;
  }

  if (!todo.value.priority) {
    errors.value.priority = '必须指定一个优先级';
    isValid = false;
  }

  return isValid;
};

const updateTodo = async () => {
  if (validateForm()) {
    try {
      await store.dispatch('verifyToken');
      if (!store.state.isTokenValid) {
        alert('登录状态已超时，请重新登录！');
        await store.dispatch('logout');
        await router.push('/login');
        return;
      }
      const response = await fetch(serverURL + '/todo', {
        method: 'PUT',
        headers: {
          'Authorization': 'Bearer ' + localStorage.getItem('jwt-token'),
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(todo.value)
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      console.log(data);
      successMessage.value = '已更新待办内容!';
      errorMessage.value = '';
      setTimeout(() => {
        router.push('/');
      }, 1000);
    } catch (error) {
      errorMessage.value = '更新待办失败，请检查网络!';
      successMessage.value = '';
    }
  }
};

onMounted(() => {
  getTodo();
});
</script>
