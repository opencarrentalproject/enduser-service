<template>
    <div class="list row">
        <div class="col-md-8">
            <div class="input-group mb-3">
                <input type="text" class="form-control" placeholder="Search by title"
                       v-model="email"/>
                <div class="input-group-append">
                    <button class="btn btn-outline-secondary" type="button"
                            @click="searchTitle">
                        Search
                    </button>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <h4>Users</h4>
            <table class="table">
                <thead class="thead-dark">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">FirstName</th>
                    <th scope="col">LastName</th>
                    <th scope="col">Email</th>
                    <th scope="col"/>
                    <th scope="col"/>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(user, index) in users" :key="index">
                    <th scope="row">{{index}}</th>
                    <td>{{user.firstName}}</td>
                    <td>{{user.lastName}}</td>
                    <td>{{user.email}}</td>
                    <td><a class="btn btn-primary" :href="`/users/${user.id}`" role="button">Edit</a></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
    import UserService from "../services/UserService";

    export default {
        name: "users",
        data() {
            return {
                users: [],
                currentUser: null,
                currentIndex: -1,
                email: ""
            };
        },
        methods: {
            retrieveUsers() {
                UserService.getAll()
                    .then(response => {
                        this.users = response.data._embedded.userResourceList;
                    })
                    .catch(e => {
                        console.log(e);
                    });
            },

            refreshList() {
                this.retrieveUsers();
                this.currentUser = null;
                this.currentIndex = -1;
            },

            removeUser(user) {
                UserService.delete(user.id)
                    .then(response => {
                        console.log(response.data);
                        this.refreshList();
                    })
                    .catch(e => {
                        console.log(e);
                    });
            },

            searchTitle() {
                // todo implement search
            }
        },
        mounted() {
            this.retrieveUsers();
        }
    };
</script>

<style>
    .list {
        text-align: left;
        max-width: 750px;
        margin: auto;
    }
</style>
