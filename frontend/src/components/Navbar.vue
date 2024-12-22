<template>
  <main>
    <nav class="navbar navbar-expand-lg bg-body-tertiary full-width-navbar">
      <div class="container-fluid">
        <div class="collapse navbar-collapse" id="navbarSupportContent">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <a href="/" class="nav-link">查看待办</a>
            </li>
            <li class="nav-item">
              <a href="/add" class="nav-link">添加待办</a>
            </li>
            <li class="nav-item">
              <button class="nav-link" @click="logout">退出账号</button>
            </li>
          </ul>
        </div>
        <div id="welcome" class="user-text">欢迎您，{{ userName }}！(账号：{{ userId }})</div>
      </div>
    </nav>
  </main>
</template>

<style src="../assets/navbar.css"></style>

<script>
import store from '../store/index';

export default {
  name: 'Navbar',
  computed: {
    userName() {
      return localStorage.getItem('user-name');
    },
    userId() {
      return localStorage.getItem('user-id');
    }
  },
  methods: {
    logout() {
      store.state.token = null;
      store.state.userId = null;
      store.state.isTokenValid = null;
      store.state.userName = null;
      localStorage.removeItem('jwt-token');
      localStorage.removeItem('user-id');
      localStorage.removeItem('isTokenValid');
      localStorage.removeItem('user-name');
      this.$router.push('/login');
    }
  }
}
</script>
