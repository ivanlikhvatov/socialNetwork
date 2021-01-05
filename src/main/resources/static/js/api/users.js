import Vue from 'vue'

const users = Vue.resource('/user{/id}')

export default {
    // add: user => users.save({}, user),
    update: user => users.update({id: user.id}, user),
    remove: id => users.remove({id}),
    list: list => Vue.http.get('/user')
}