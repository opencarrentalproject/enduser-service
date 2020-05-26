import Vue from 'vue'
import VueRouter from 'vue-router'
import Users from '../components/Users.vue'
import Login from '../components/Login.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'users',
        component: Users,
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
        component: () => import(/* webpackChunkName: "about" */ '../components/UserDetail.vue'),
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
    routes
})

router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requireAuth)) {
        if (localStorage.getItem('jwt') == null) {
            next({
                path: '/login',
                params: { nextUrl: to.fullPath }
            })
        } else {
            next()
        }
    } else if(to.matched.some(record => record.meta.anonymous)) {
        if(localStorage.getItem('jwt') == null){
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
