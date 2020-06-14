<template>
   <div v-if="currentUser" class="edit-form">
      <h4>User</h4>
      <form>
         <div class="form-group">
            <label for="title">FirstName</label>
            <input type="text" class="form-control" id="title"
                   v-model="currentUser.firstName"
            />
         </div>
         <div class="form-group">
            <label for="lastname">LastName</label>
            <input type="text" class="form-control" id="lastname"
                   v-model="currentUser.lastName"
            />
         </div>
         <div class="form-group">
            <label for="role">Role</label>
            <select v-model="selectedRole" class="custom-select" id="role">
               <option :selected="true" :value="selectedRole">{{selectedRole? selectedRole.name : emptyRole}}</option>
               <option v-for="(role) in roles" :key=role.id :value="role">
                  {{role.name}}
               </option>
            </select>
         </div>
      </form>

      <button class="btn btn-danger mr-3"
              @click="deleteUser"
      >
         Delete
      </button>

      <button type="submit" class="btn btn-success"
              @click="updateUser"
      >
         Update
      </button>
      <p>{{ message }}</p>
   </div>

   <div v-else>
      <br />
      <p>No user found</p>
   </div>
</template>

<script>
   import UserService from "../services/UserService";
   import RoleService from "../services/RoleService";

   export default {
      name: "UserDetail",
      data() {
         return {
            currentUser: null,
            selectedRole: {
               id: null,
               name: null
            },
            message: '',
            roles: [],
            emptyRole: "Assign a role to user"
         };
      },
      methods: {
         getUser(id) {
            UserService.get(id)
                    .then(response => {
                       let user = response.data;
                       this.currentUser = user;
                       console.log(user.roles);
                       this.selectedRole = user.roles[0]
                       console.log(response.data);
                    })
                    .catch(e => {
                       console.log(e);
                    });
         },

         updateUser() {
            if (this.selectedRole && this.selectedRole.name !== this.emptyRole) {
               this.currentUser.roles = Array(this.selectedRole)
            }
            UserService.update(this.currentUser.id, this.currentUser)
                    .then(response => {
                       console.log(response.data);
                       this.$router.push({ name: "users" });
                    })
                    .catch(e => {
                       console.log(e);
                    });
         },

         deleteUser() {
            UserService.delete(this.currentUser.id)
                    .then(response => {
                       console.log(response.data);
                       this.$router.push({ name: "users" });
                    })
                    .catch(e => {
                       console.log(e);
                    });
         },
         retrieveRoles() {
            RoleService.getAll()
            .then(response => {
               this.roles = response.data._embedded.roleResourceList;
            })
            .catch(e => {
               console.log(e);
            })
         }
      },
      mounted() {
         this.message = '';
         this.getUser(this.$route.params.id);
         this.retrieveRoles();
      }
   };
</script>

<style>
   .edit-form {
      max-width: 300px;
      margin: auto;
   }
</style>