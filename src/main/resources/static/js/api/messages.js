import Vue from 'vue'

const messages = Vue.resource('/message{/id}')

export default {
    addGeneral: message => messages.save({id: "GENERAL"}, message),
    addPrivate: message => messages.save({id: "PRIVATE"}, message),
    addGroup: message => messages.save({id: "GROUP"}, message),
    update: message => messages.update({id: message.id}, message),
    remove: id => messages.remove({id}),
    generalMessagePage: page => Vue.http.get('/message/general', {params: { page }}),
    privateMessagePage: page => Vue.http.get('/message/private', {params: { page }}),
    groupMessagePage: page => Vue.http.get('/message/group', {params: { page }})
}