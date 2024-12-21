// Import Vue Router functions and components
import { createRouter, createWebHistory } from 'vue-router';
import { useStore } from 'vuex'

// Create and configure the router
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL), // Use HTML5 History API
  routes: [
    {
      path: '/login', // Path for the login page
      name: 'login', // Route name
      component: () => import('../views/Login.vue') // Lazy-load component
    },
    {
      path: '/register', // Path for the register page
      name: 'register', // Route name
      component: () => import('../views/Register.vue') // Lazy
    },
    {
      path: '/', // Root path
      name: 'home', // Route name
      component: () => import('../views/ViewTodo.vue'), // Component for this route
      meta: { requiresAuth: true }
    },
    {
      path: '/add', // Path for adding a new todo
      name: 'add', // Route name
      component: () => import('../views/AddTodo.vue'), // Lazy-load component
      meta: { requiresAuth: true }
    },
    {
      path: '/edit/:id', // Path for editing a specific todo
      name: 'edit', // Route name
      component: () => import('../views/UpdateTodo.vue'), // Lazy-load component
      meta: { requiresAuth: true }
    }
  ]
});

// Add a navigation guard to check if the route requires authentication
router.beforeEach((to, from, next) => {
  console.log("To route:", to.name);
  const store = useStore()
  if (to.matched.some(record => record.meta.requiresAuth)) {
    store.dispatch('verifyToken');
    if (store.getters.isAuthorized) {
      next();
    } else {
      next('/login'); // 如果 token 无效，跳转到登录页面
    }
  } else {
    next();
  }
});

// Export the router instance
export default router;
