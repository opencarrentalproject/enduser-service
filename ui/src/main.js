import Vue from 'vue'
import App from './App.vue'
import http from './utils/http-common';
import router from './router'

Vue.config.productionTip = false
Vue.prototype.$http = http
http.defaults.timeout = 5000

// Configure interceptor
http.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        console.log("intercepting request");
        if (token) {
            config.headers.common['Authorization'] = "Bearer " + token;
        }
        return config;
    }, error => {
        return Promise.reject(error);
    }
);

http.interceptors.response.use(
    response => {
        if (response.status === 200 || response.status === 201) {
            return Promise.resolve(response);
        } else {
            return Promise.reject(response);
        }
    },
    error => {
        if (error.response.status) {
            switch (error.response.status) {
                case 400:
                    //todo show error messagee
                    break;
                case 401:
                    localStorage.removeItem('token')
                    router.push('/login')
                    break;
                case 403:
                    router.replace({
                        path: "/login",
                        query: { redirect: router.currentRoute.fullPath }
                    });
                    break;
                case 404:
                    //todo show notfound page
                    break;
                case 502:
                    setTimeout(() => {
                        router.replace({
                            path: "/login",
                            query: {
                                redirect: router.currentRoute.fullPath
                            }
                        });
                    }, 1000);
            }
            return Promise.reject(error.response);
        }
    }
);

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
