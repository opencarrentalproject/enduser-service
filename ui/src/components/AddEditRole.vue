<template>
    <div class="submit-form">
        <div>
            <div v-if="errors.length">
                <p class="font-weight-bold text-danger">
                    <b>Please correct the following error(s):</b>
                <ul>
                    <li v-for="(error, index) in errors" :key=index>{{ error }}</li>
                </ul>
                </p>
            </div>
            <div class="form-group">
                <label for="name">Name</label>
                <input
                        class="form-control"
                        id="name"
                        required
                        v-model="role.name"
                        name="name"
                />
            </div>
            <button @click="onSubmit" class="btn btn-success">Submit</button>
        </div>
    </div>
</template>

<script>
    import RoleService from "../services/RoleService";
    export default {
        name: "add-edit-role",
        props: ["isEdit", "passedRoleName", "passedRoleId"],
        data() {
            return {
                role: {
                    name: this.passedRoleName,
                    id: this.passedRoleId
                },
                errors: []
            };
        },
        methods: {
            onSubmit() {
                if (this.isEdit) {
                    this.editRole()
                } else {
                    this.saveRole()
                }
            },
            saveRole() {
                const data = {
                    name: this.role.name
                };
                RoleService.create(data)
                    .then(response => {
                        console.log(response.data);
                        this.$emit('roleSaved')
                    })
                    .catch(e => {
                        console.log(e);
                    });
            },
            editRole() {
                const data = {
                    name: this.role.name
                };
                RoleService.update(this.role.id, data)
                    .then(response => {
                        console.log(response.data);
                        this.$emit('roleSaved')
                    })
                    .catch(e => {
                        console.log(e);
                    });
            },
        }
    };
</script>

<style>
    .submit-form {
        max-width: 300px;
        margin: auto;
    }
</style>