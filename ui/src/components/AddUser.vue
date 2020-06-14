<template>
    <div class="submit-form">
        <div v-if="!submitted">
            <div v-if="errors.length">
                 <p class="font-weight-bold text-danger">
                    <b>Please correct the following error(s):</b>
                    <ul>
                        <li v-for="(error, index) in errors" :key=index>{{ error }}</li>
                    </ul>
                </p>
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input
                        type="email"
                        class="form-control"
                        id="email"
                        required
                        v-model="user.email"
                        name="email"
                />
            </div>

            <div class="form-group">
                <label for="firstName">FirstName</label>
                <input
                        class="form-control"
                        id="firstName"
                        required
                        v-model="user.firstName"
                        name="firstName"
                />
            </div>

            <div class="form-group">
                <label for="lastName">LastName</label>
                <input
                        class="form-control"
                        id="lastName"
                        required
                        v-model="user.lastName"
                        name="lastName"
                />
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input
                        class="form-control"
                        id="password"
                        required
                        v-model="user.password"
                        type="password"
                        name="password"
                />
            </div>

            <div class="form-group">
                <label for="passwordConfirm">Confirm Password</label>
                <input
                        class="form-control"
                        id="passwordConfirm"
                        required
                        v-model="passwordConfirm"
                        type="password"
                        name="passwordConfirm"
                />
            </div>

            <button @click="saveUser" class="btn btn-success">Submit</button>
        </div>

        <div v-else>
            <h4>User successfully created</h4>
            <button class="btn btn-success" @click="newUser">Add</button>
        </div>
    </div>
</template>

<script>
    import UserService from "../services/UserService";
    export default {
        name: "add-user",
        data() {
            return {
                user: {
                    email: "",
                    firstName: "",
                    lastName: "",
                    password: "",
                },
                passwordConfirm: "",
                submitted: false,
                errors: []
            };
        },
        methods: {
            saveUser() {
                const data = {
                    email: this.user.email,
                    firstName: this.user.firstName,
                    lastName: this.user.lastName,
                    password: this.user.password
                };
                if (data.password !== this.passwordConfirm) {
                    this.errors.push('Password and Confirm do not match');
                    return;
                }
                UserService.create(data)
                    .then(response => {
                        console.log(response.data);
                        this.submitted = true;
                    })
                    .catch(e => {
                        console.log(e);
                    });
            },

            newUser() {
                this.submitted = false;
                this.user = {};
            }
        }
    };
</script>

<style>
    .submit-form {
        max-width: 300px;
        margin: auto;
    }
</style>