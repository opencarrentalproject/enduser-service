import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../components/Login.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'users',
        alias: '/users',
        component: () => import('../components/Users.vue'),
        meta: {
            requireAuth: true
        }
    },
    {
        path: '/login',
        name: 'login',
        component: Login,
        meta: {
            anonymous: true
        }
    },
    {
        path: '/users/:id',
        name: 'user-details',
        component: () => import('../components/UserDetail.vue'),
        meta: {
            requireAuth: true
        }
    },
    {
        path: '/add',
        name: 'add',
        component: () => import('../components/AddUser.vue'),
        meta: {
            requireAuth: true
        }
    }
]

const router = new VueRouter({
    mode: "history",
    routes
})

router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requireAuth)) {
        if (localStorage.getItem('token') == null) {
            next({
                path: '/login',
                params: { nextUrl: to.fullPath }
            })
        } else {
            next()
        }
    } else if(to.matched.some(record => record.meta.anonymous)) {
        if(localStorage.getItem('token') == null){
            next()
        }
        else{
            next({ name: 'users'})
        }
    } else {
        next();
    }
});

export default router
