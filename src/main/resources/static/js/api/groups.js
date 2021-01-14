import Vue from 'vue'

const groups = Vue.resource('/group{/id}')

export default {
    create: group => groups.save({}, group),
    update: group => groups.update({id: group.id}, group)
}