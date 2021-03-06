import Vue from 'vue'
import VueSocial from "@growthbunker/vuesocial";
import Vuetify from "vuetify";
import '@babel/polyfill'
import 'api/resource'
import router from "router/router";
import App from 'pages/App.vue'
import store from 'store/store'
import { connect } from 'util/ws'
import 'vuetify/dist/vuetify.min.css'

if (profile){
    connect();
}

Vue.use(Vuetify);
Vue.use(VueSocial);

new Vue({
    el: '#app',
    vuetify : new Vuetify(),
    router,
    store,
    render: a => a(App),
});
