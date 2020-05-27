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
            <label for="description">LastName</label>
            <input type="text" class="form-control" id="description"
                   v-model="currentUser.lastName"
            />
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
      <p>Please click on a Tutorial...</p>
   </div>
</template>

<script>
   import UserService from "../services/UserService";

   export default {
      name: "UserDetail",
      data() {
         return {
            currentUser: null,
            message: ''
         };
      },
      methods: {
         getUser(id) {
            UserService.get(id)
                    .then(response => {
                       this.currentUser = response.data;
                       console.log(response.data);
                    })
                    .catch(e => {
                       console.log(e);
                    });
         },

         updateUser() {
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
         }
      },
      mounted() {
         this.message = '';
         this.getUser(this.$route.params.id);
      }
   };
</script>

<style>
   .edit-form {
      max-width: 300px;
      margin: auto;
   }
</style>