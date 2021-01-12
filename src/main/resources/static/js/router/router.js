import Vue from 'vue'
import VueRouter from "vue-router";
import GeneralMessagesList from 'pages/GeneralMessageList.vue'
import PrivateMessageList from "pages/PrivateMessageList.vue";
import Auth from "pages/Auth.vue";
import Profile from "pages/Profile.vue";
import Registration from "pages/Registration.vue";
import UserList from "pages/UserList.vue";
import UserInfo from "pages/UserInfo.vue"

Vue.use(VueRouter);

const routes = [
    { path: '/', component: GeneralMessagesList },
    { path: '/userList', component: UserList },
    { path: '/userInfo', component: UserInfo, props: true, params: true, name: 'userInfo'},
    { path: '/auth', component: Auth },
    { path: '/profile', component: Profile },
    { path: '/registration', component: Registration },
    { path: '/loginError', component: Auth },
    { path: '/login', component: Auth },
    { path: '/privateMessages', component: PrivateMessageList, props: true, params: true, name: "privateMessages"},
    { path: '*', component: GeneralMessagesList },
];

export default new VueRouter({
    mode: 'history',
    routes,
});