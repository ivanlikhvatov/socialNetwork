import Vue from 'vue'

const groups = Vue.resource('/group{/id}')

export default {
    create: group => groups.save({}, group),
    // update: message => groups.update({id: 'addMessage'}, message)
}