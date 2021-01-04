import Vue from 'vue'
import VueRouter from "vue-router";
import MessagesList from 'pages/MessageList.vue'
import Auth from "pages/Auth.vue";
import Profile from "pages/Profile.vue";
import Registration from "pages/Registration.vue";

Vue.use(VueRouter);

const routes = [
    { path: '/', component: MessagesList},
    { path: '/auth', component: Auth },
    { path: '/profile', component: Profile },
    { path: '/registration', component: Registration },
    { path: '/loginError', component: Auth },
    { path: '/login', component: Auth },
    { path: '*', component: MessagesList },
];

export default new VueRouter({
    mode: 'history',
    routes,
});