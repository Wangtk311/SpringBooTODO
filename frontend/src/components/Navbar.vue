<template>
  <main>
    <!-- Navbar component with full-width styling -->
    <nav class="navbar navbar-expand-lg bg-body-tertiary full-width-navbar">
      <div class="container-fluid">
        <div class="collapse navbar-collapse" id="navbarSupportContent">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <!-- Navigation items -->
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
      </div>
    </nav>
  </main>
</template>

<!-- Import the external CSS file -->
<style src="../assets/navbar.css"></style>

<script>
import store from '../store/index';
export default {
  name: 'Navbar',
  methods: {
    logout() {
      // Clear the token and user information
      store.state.token = null;
      store.state.user = null;
      store.state.isTokenValid = null;
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('isTokenValid');
      // Redirect to the login page
      this.$router.push('/login');
    }
  }
}
</script>
