<template>
    <div>
        <h4>Login</h4>
        <form>
            <label for="username" >Username</label>
            <div>
                <input id="username" type="text" v-model="username" required autofocus>
            </div>
            <div>
                <label for="password" >Password</label>
                <div>
                    <input id="password" type="password" v-model="password" required>
                </div>
            </div>
            <div>
                <button type="submit" @click="handleSubmit">
                    Login
                </button>
            </div>
        </form>
    </div>
</template>

<script>
    import AuthService from "../services/AuthService";
    export default {
        name: "Login",
        data(){
            return {
                username: "",
                password: ""
            }
        },
        methods: {
            handleSubmit(event) {
                event.preventDefault();
                if (this.password.length > 0) {
                    AuthService.login(this.username, this.password)
                    .then(response => {
                        const token = response.data;
                        localStorage.setItem('token', token);
                        if (localStorage.getItem('token') != null){
                            this.$emit('loggedIn');
                            if (this.$route.params.nextUrl != null) {
                                this.$router.push(this.$route.params.nextUrl)
                            } else {
                                this.$router.push('users')
                            }
                        }
                    }).catch(error => {
                        console.error(error);
                    });
                }
            }
        }
    }
</script>

<style scoped>

</style>