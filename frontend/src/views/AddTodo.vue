<template>
    <main>
        <Navbar/>   
        <div class="my-5">
            <div class="mx-auto" style="max-width: 500px; width: 100%;">
                <h1 class="text-center mb-4">添加新待办</h1>
                <form @submit.prevent="addTodo">
                    <!-- Hidden ID Field -->
                    <input type="hidden" v-model="todo.id">

                    <!-- Title -->
                    <div class="row">
                        <div class="col-md-12 form-group mb-3">
                            <label for="title" class="form-label" style="font-weight: bold;">待办标题</label>
                            <input type="text" name="title" id="title" class="form-control" placeholder="待办标题" required v-model="todo.title">
                            <span class="text-danger" v-if="errors.title">{{ errors.title }}</span>
                        </div>
                    </div>

                    <!-- Description -->
                    <div class="row">
                        <div class="col-md-12 form-group mb-3">
                            <label for="description" class="form-label" style="font-weight: bold;">待办内容</label>
                            <input type="text" name="description" id="description" class="form-control" placeholder="待办内容" required v-model="todo.description">
                            <span class="text-danger" v-if="errors.description">{{ errors.description }}</span>
                        </div>
                    </div>
                    
                    <!-- Date Field -->
                    <div class="row">
                        <div class="col-md-12 form-group mb-3">
                            <label for="date" class="form-label" style="font-weight: bold;">截止日期</label>
                            <input type="date" name="date" id="date" class="form-control" required v-model="todo.date"/>
                            <span class="text-danger" v-if="errors.date">{{ errors.date }}</span>
                        </div>
                    </div>

                     <!-- Priority Field -->
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
                    
                    <!-- Hidden Fields for Completion Status -->
                    <input type="hidden" v-model="todo.completed">
                
                     <!-- Submit Button -->
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary w-100">确认</button>
                    </div>
                    
                    <!-- Success/Error Message -->
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
import Navbar from '../components/Navbar.vue';  // Import the Navbar component
import '../assets/styles.css'; // Import the CSS file

export default {
    name: 'AddTodo',
    components: {
        Navbar  // Register the Navbar component
    },

    data() {
        return {  
            // Initial state for the todo object
            todo: {
                id: null,
                title: '',
                description: '',
                date: '',
                priority: '低', // Default priority
                completed: 'false' // Default completed
            },
            errors: {},
            successMessage: '',
            errorMessage: ''
        }
    },

    methods: {
    // Method to validate the form fields   
    validateForm() {
        this.errors = {};  // Reset errors
        let isValid = true;

        // Title validation
        if (!this.todo.title.trim()) {
            this.errors.title = '必须提供一个标题';
            isValid = false;
        } else if (this.todo.title.length < 2) {
            this.errors.title = '标题不能少于两个字符';
            isValid = false;
        }

        // Description validation
        if (!this.todo.description.trim()) {
            this.errors.description = '必须提供一段内容';
            isValid = false;
        } else if (this.todo.description.length < 3) {
            this.errors.description = '内容不能少于三个字符';
            isValid = false;
        }

        // Date validation
        if (!this.todo.date) {
            this.errors.date = '必须提供一个截止日期';
            isValid = false;
        } else if (new Date(this.todo.date) < new Date()) {
            this.errors.date = '不能选择过去的时间';
            isValid = false;
        }

        // Priority validation (optional)
        if (!this.todo.priority) {
            this.errors.priority = '必须指定一个优先级';
            isValid = false;
        }

        return isValid; // Return the validity status of the form
    },

    // Method to add a new todo item
    addTodo() {
        if (this.validateForm()) {  // Only proceed if the form is valid
            // Send a POST request to the server to add a new todo
            fetch('http://localhost:8080/todo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'  // Send data as JSON
                },
                body: JSON.stringify(this.todo)
            })
            // Handle the server response
            .then(res => {
                if (!res.ok) {
                    throw new Error('Network response was not ok'); // Handle network errors
                }
                return res.text();  // Return response text
            })
            .then(data => {
                this.successMessage = 'Todo added successfully!';   // Success message
                this.errorMessage = ''; // Clear any previous errors
                this.$router.push("/home"); // Redirect to the home page
            })  
            .catch(error => {
                this.errorMessage = 'Error adding todo: ' + error.message;  // Error message
                this.successMessage = ''; // Clear any previous success messages
            });
        }
    }
}
}
</script>