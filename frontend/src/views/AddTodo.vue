<template>
  <main>
    <Navbar/>
    <div class="my-5">
      <div class="mx-auto" style="max-width: 500px; width: 100%;">
        <h1 class="text-center mb-4">添加新待办</h1>
        <form @submit.prevent="addTodo">
          <input type="hidden" v-model="todo.id">
          <div class="row">
            <div class="col-md-12 form-group mb-3">
              <label for="title" class="form-label" style="font-weight: bold;">待办标题</label>
              <input type="text" name="title" id="title" class="form-control" placeholder="待办标题" required v-model="todo.title">
              <span class="text-danger" v-if="errors.title">{{ errors.title }}</span>
            </div>
          </div>
          <div class="row">
            <div class="col-md-12 form-group mb-3">
              <label for="description" class="form-label" style="font-weight: bold;">详细内容</label>
              <input type="text" name="description" id="description" class="form-control" placeholder="待办内容" required v-model="todo.description">
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
          <input type="hidden" v-model="todo.completed">
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

<script>
import Navbar from '../components/Navbar.vue';
import '../assets/styles.css';
import store from "@/store/index.js";
import { serverURL } from '../serverURLConfig.js';

export default {
  name: 'AddTodo',
  components: {
    Navbar
  },

  data() {
    return {
      todo: {
        id: null,
        title: '',
        description: '',
        date: '',
        priority: '低',
        completed: 'false',
        userid: localStorage.getItem('user-id')
      },
      errors: {},
      successMessage: '',
      errorMessage: ''
    }
  },

  methods: {
    validateForm() {
      this.errors = {};
      let isValid = true;

      if (!this.todo.title.trim()) {
        this.errors.title = '必须提供一个标题';
        isValid = false;
      } else if (this.todo.title.length < 2) {
        this.errors.title = '标题不应少于两个字符';
        isValid = false;
      }

      if (!this.todo.description.trim()) {
        this.errors.description = '必须提供一段内容';
        isValid = false;
      } else if (this.todo.description.length < 2) {
        this.errors.description = '内容不应少于两个字符';
        isValid = false;
      }

      if (!this.todo.date) {
        this.errors.date = '必须提供一个截止日期';
        isValid = false;
      } else if (new Date(this.todo.date) < new Date()) {
        this.errors.date = '不应选择过去的时间';
        isValid = false;
      }

      if (!this.todo.priority) {
        this.errors.priority = '必须指定一个优先级';
        isValid = false;
      }

      return isValid;
    },

    addTodo() {
      if (this.validateForm()) {
        store.dispatch('verifyToken');
        if (!store.state.isTokenValid) {
          alert('登录状态已超时，请重新登录！');
          store.dispatch('logout');
          this.$router.push('/login');
        } else {
          fetch(serverURL + '/todo', {
            method: 'POST',
            headers: {
              'Authorization': 'Bearer ' + localStorage.getItem('jwt-token'),
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(this.todo)
          })
              .then(res => {
                if (!res.ok) {
                  throw new Error('Network response was not ok');
                }
                return res.text();
              })
              .then(data => {
                this.successMessage = '成功添加新待办!';
                this.errorMessage = '';
                setTimeout(() => {
                  this.$router.push("/");
                }, 1000);
              })
              .catch(error => {
                this.errorMessage = '添加待办失败，请检查网络!';
                this.successMessage = '';
              });
        }
      }
    }
  }
}
</script>