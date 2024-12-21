<template>
  <main class="full-height">
    <Navbar />

    <div class="full-width-container">
      <h1 class="text-center mb-4">{{ getUserName() }}的所有待办</h1>
      <div class="add-and-sort">
        <div class="sort-container">
          <a href="/add" class="btn btn-primary">添加待办</a>
          <select id="sortBy" v-model="sortMethod" @change="sortTodos">
            <option value="priority">按优先级别排序</option>
            <option value="date">按截止时间排序</option>
          </select>
        </div>
      </div>
      <div class="uncompleted-text">未完成待办</div>
      <div class="table-container-no" v-if="uncomptodos.length > 0">
        <table class="table">
          <thead>
          <tr>
            <th scope="col" style="font-weight: bold; width: 80px">优先级</th>
            <th scope="col" style="font-weight: bold; width: 80px">状态</th>
            <th scope="col" style="font-weight: bold; width: 200px">待办标题</th>
            <th scope="col" style="font-weight: bold; width: 120px">截止时间</th>
            <th scope="col" style="font-weight: bold; width: 810px">详细内容</th>
            <th scope="col" style="font-weight: bold;">是否完成</th>
            <th scope="col" style="font-weight: bold;"> </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="todo in uncomptodos" :key="todo.id">
            <td>
              <div :class="['priority-box', priorityClass(todo.priority)]">
                {{ todo.priority }}
              </div>
            </td>
            <td :class="getStatusClass(todo.status)">{{ todo.status }}</td>
            <td>{{ todo.title }}</td>
            <td :class="getDeadlineClass(todo.date)">{{ todo.date }}</td>
            <td>{{ todo.description }}</td>
            <td>
              <input type="checkbox" class="large-checkbox" :checked="todo.completed" @change="toggleCompletion(todo)"/>
            </td>
            <td>
              <a class="btn btn-primary" :href="`/edit/${todo.id}`">编辑</a>
              <button class="btn btn-danger mx-2" @click="deleteTodo(todo.id)">删除</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class="no-todos" v-else>
        暂无未完成待办
      </div>
      <div class="completed-text">已完成待办</div>
      <div class="table-container-yes" v-if="comptodos.length > 0">
        <table class="table">
          <thead>
          <tr>
            <th scope="col" style="font-weight: bold; width: 80px">优先级</th>
            <th scope="col" style="font-weight: bold; width: 80px">状态</th>
            <th scope="col" style="font-weight: bold; width: 200px">待办标题</th>
            <th scope="col" style="font-weight: bold; width: 120px">截止时间</th>
            <th scope="col" style="font-weight: bold; width: 810px">详细内容</th>
            <th scope="col" style="font-weight: bold;">是否完成</th>
            <th scope="col" style="font-weight: bold;"> </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="todo in comptodos" :key="todo.id">
            <td>
              <div :class="['priority-box', priorityClass(todo.priority)]">
                {{ todo.priority }}
              </div>
            </td>
            <td :class="getStatusClass(todo.status)">{{ todo.status }}</td>
            <td>{{ todo.title }}</td>
            <td>{{ todo.date }}</td>
            <td>{{ todo.description }}</td>
            <td>
              <input type="checkbox" class="large-checkbox" :checked="todo.completed" @change="toggleCompletion(todo)"/>
            </td>
            <td>
              <a class="btn btn-primary" :href="`/edit/${todo.id}`">编辑</a>
              <button class="btn btn-danger mx-2" @click="deleteTodo(todo.id)">删除</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class="no-todos" v-else>
        暂无已完成待办
      </div>
    </div>
  </main>
</template>

<script>
import Navbar from '../components/Navbar.vue';
import '../assets/styles.css';
import store from '../store/index';
import { serverURL } from '../serverURLConfig.js';

export default {
  name: 'ViewTodos',
  computed: {
    Navbar() {
      return Navbar
    }
  },
  components: {
    Navbar
  },
  data() {
    return {
      uncomptodos: [],
      comptodos: [],
      sortMethod: 'priority',
    }
  },
  methods: {
    getStatusClass(status) {
      if (status === '在将来') {
        return 'status-future';
      } else if (status === '已完成') {
        return 'status-completed';
      } else if (status === '已逾期') {
        return 'status-overdue';
      } else if (status === '较紧迫') {
        return 'status-urgent';
      }
      return '';
    },

    getDeadlineClass(date) {
      const currentDate = new Date();
      const deadlineDate = new Date(date);
      const timeDifference = deadlineDate - currentDate;
      const threeDaysInMillis = 3 * 24 * 60 * 60 * 1000;

      if (timeDifference <= threeDaysInMillis && timeDifference > 0) {
        return 'deadline-urgent';
      } else if (deadlineDate < currentDate) {
        return 'deadline-overdue';
      }
      return 'deadline-future';
    },

    getTodos() {
      store.dispatch('verifyToken');
      if (!store.state.isTokenValid) {
        alert('登录状态已超时，请重新登录！');
        store.dispatch('logout');
        this.$router.push('/login');
        return;
      }

      const url1 = new URL(serverURL + '/todouncomplete');
      const param1 = { userid: localStorage.getItem('user-id') };
      Object.keys(param1).forEach(key => url1.searchParams.append(key, param1[key]));
      fetch(url1, {
        method: 'GET',
        headers: {
          'Authorization': 'Bearer ' + localStorage.getItem('jwt-token'),
          'Content-Type': 'form-data'
        }
      }).then(res => {
        if (!res.ok) {
          throw new Error('Network response was not ok');
        }
        return res.json();
      }).then(data => {
        this.uncomptodos = data.map(todo => ({
          ...todo,
          status: this.getTodoStatus(todo.date)
        }));
        this.sortTodos();
      }).catch(error => console.error('获取待办失败:', error));

      store.dispatch('verifyToken');
      if (!store.state.isTokenValid) {
        alert('登录状态已超时，请重新登录！');
        store.dispatch('logout');
        this.$router.push('/login');
        return;
      }

      const url2 = new URL(serverURL + '/todocompleted');
      const param2 = { userid: localStorage.getItem('user-id') };
      Object.keys(param2).forEach(key => url2.searchParams.append(key, param2[key]));
      fetch(url2, {
        method: 'GET',
        headers: {
          'Authorization': 'Bearer ' + localStorage.getItem('jwt-token'),
          'Content-Type': 'form-data'
        }
      }).then(res => {
        if (!res.ok) {
          throw new Error('Network response was not ok');
        }
        return res.json();
      }).then(data => {
        this.comptodos = data.map(todo => ({
          ...todo,
          status: '已完成'
        }));
        this.sortTodos();
      }).catch(error => console.error('获取待办失败:', error));
      this.checkTodosStatus();
    },

    getTodoStatus(date) {
      const currentDate = new Date();
      const deadlineDate = new Date(date);
      const timeDifference = deadlineDate - currentDate;
      const threeDaysInMillis = 3 * 24 * 60 * 60 * 1000;

      if (timeDifference <= threeDaysInMillis && timeDifference > 0) {
        return '较紧迫';
      } else if (deadlineDate < currentDate) {
        return '已逾期';
      } else {
        return '在将来';
      }
    },

    checkTodosStatus() {
      const currentDate = new Date();
      this.uncomptodos.forEach(todo => {
        const deadlineDate = new Date(todo.date);
        const timeDifference = deadlineDate - currentDate;
        const threeDaysInMillis = 3 * 24 * 60 * 60 * 1000;

        if (timeDifference <= threeDaysInMillis && timeDifference > 0) {
          todo.status = '较紧迫';
        } else if (deadlineDate < currentDate && todo.status !== '已逾期') {
          todo.status = '已逾期';
        } else if (deadlineDate > currentDate && todo.status !== '较紧迫') {
          todo.status = '在将来';
        }
      });
    },

    sortTodos() {
      if (this.sortMethod === 'priority') {
        this.uncomptodos = this.sortTodosByPriorityAndDate(this.uncomptodos);
        this.comptodos = this.sortTodosByPriorityAndDate(this.comptodos);
      } else if (this.sortMethod === 'date') {
        this.uncomptodos = this.sortTodosByDateAndPriority(this.uncomptodos);
        this.comptodos = this.sortTodosByDateAndPriority(this.comptodos);
      }
    },

    sortTodosByPriorityAndDate(todos) {
      const priorityOrder = { '高': 3, '中': 2, '低': 1 };
      return todos.sort((a, b) => {
        const priorityComparison = priorityOrder[b.priority] - priorityOrder[a.priority];
        if (priorityComparison !== 0) return priorityComparison;
        return new Date(a.date) - new Date(b.date);
      });
    },

    sortTodosByDateAndPriority(todos) {
      return todos.sort((a, b) => {
        const dateComparison = new Date(a.date) - new Date(b.date);
        if (dateComparison !== 0) return dateComparison;
        const priorityOrder = { '高': 3, '中': 2, '低': 1 };
        return priorityOrder[b.priority] - priorityOrder[a.priority];
      });
    },

    deleteTodo(id) {
      store.dispatch('verifyToken');
      if (!store.state.isTokenValid) {
        alert('登录状态已超时，请重新登录！');
        store.dispatch('logout');
        this.$router.push('/login');
        return;
      }
      if (!confirm('确定要删除这个待办吗？此操作无法撤销！')) {
        return;
      }
      const url = new URL(serverURL + `/todo/${id}`);
      const param = { userid: localStorage.getItem('user-id') };
      Object.keys(param).forEach(key => url.searchParams.append(key, param[key]));
      fetch(url, {
        method: 'DELETE',
        headers: {
          'Authorization': 'Bearer ' + localStorage.getItem('jwt-token'),
          'Content-Type': 'from-data'
        }
      })
          .then(res => {
            if (!res.ok) {
              throw new Error(`HTTP error! status: ${res.status}`);
            }
            return res.text();
          })
          .then(data => {
            this.getTodos();
          })
          .catch(error => console.error('删除待办失败:', error));
    },

    toggleCompletion(todo) {
      store.dispatch('verifyToken');
      if (!store.state.isTokenValid) {
        alert('登录状态已超时，请重新登录！');
        store.dispatch('logout');
        this.$router.push('/login');
        return;
      }
      const updatedTodo = {...todo, completed: !todo.completed};

      fetch(serverURL + `/todo`, {
        method: 'PUT',
        headers: {
          'Authorization': 'Bearer ' + localStorage.getItem('jwt-token'),
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedTodo)
      })
          .then(res => {
            if (!res.ok) {
              throw new Error('Network response was not ok');
            }
            return res.json();
          })
          .then(data => {
            this.getTodos();
          })
          .catch(error => console.error('更新待办失败:', error));
    },

    priorityClass(priority) {
      switch (priority) {
        case '高':
          return 'priority-high';
        case '中':
          return 'priority-medium';
        case '低':
          return 'priority-low';
        default:
          return '';
      }
    },

    getUserName() {
      return localStorage.getItem('user-name');
    }
  },

  mounted() {
    this.getTodos();

    setInterval(() => {
      this.checkTodosStatus();
    }, 60000);
  }
}
</script>
