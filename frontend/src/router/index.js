import { createRouter, createWebHistory } from 'vue-router';
import { useStore } from 'vuex'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/Register.vue')
    },
    {
      path: '/',
      name: 'home',
      component: () => import('../views/ViewTodo.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/add',
      name: 'add',
      component: () => import('../views/AddTodo.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/edit/:id',
      name: 'edit',
      component: () => import('../views/UpdateTodo.vue'),
      meta: { requiresAuth: true }
    }
  ]
});

router.beforeEach((to, from, next) => {
  console.log("To route:", to.name);
  const store = useStore()
  if (to.matched.some(record => record.meta.requiresAuth)) {
    store.dispatch('verifyToken');
    if (store.getters.isAuthorized) {
      next();
    } else {
      next('/login');
    }
  } else {
    next();
  }
});

export default router;
