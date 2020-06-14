<template>
    <div class="list row">
        <div class="col-md-6">
            <h4>Roles</h4>
            <table class="table">
                <thead class="thead-dark">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Name</th>
                    <th scope="col"/>
                    <th scope="col"/>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(role, index) in roles" :key="index">
                    <th scope="row">{{index}}</th>
                    <td>{{role.name}}</td>
                    <td><button class="btn btn-primary" @click="toEditRole(role)">Edit</button></td>
                </tr>
                </tbody>
            </table>
            <add-edit-role :is-edit=editMode :passed-role-name="selectedRole.name" :passed-role-id="selectedRole.id"  v-if="addEditRole" @roleSaved="onRoleSaved"/>
            <button v-else class="m-3 btn btn-sm btn-primary" @click="toAddNewRole">
                New Role
            </button>
        </div>
    </div>
</template>

<script>
    import RoleService from "../services/RoleService";
    import AddEditRole from "./AddEditRole";

    export default {
        name: "roles",
        components: {AddEditRole},
        data() {
            return {
                roles: [],
                addEditRole: false,
                editMode: false,
                selectedRole: {
                    name: '',
                    id: ''
                }
            };
        },
        methods: {
            retrieveRoles() {
                RoleService.getAll()
                    .then(response => {
                        this.roles = response.data._embedded.roleResourceList;
                    })
                    .catch(e => {
                        console.log(e);
                    });
            },

            refreshList() {
                this.retrieveRoles()
                this.selectedRole = null
                this.addEditRole = false
                this.editMode = false
            },
            removeUser(user) {
                RoleService.delete(user.id)
                    .then(response => {
                        console.log(response.data);
                        this.refreshList();
                    })
                    .catch(e => {
                        console.log(e);
                    });
            },
            toAddNewRole() {
                this.addEditRole = true
            },
            toEditRole(role) {
                this.addEditRole = true
                this.editMode = true
                this.selectedRole = role
            },
            onRoleSaved() {
                this.addEditRole = false
                this.editMode = false
                this.selectedRole = {
                    name: '',
                    id: ''
                }
                this.retrieveRoles()
        }
        },
        mounted() {
            this.retrieveRoles();
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
