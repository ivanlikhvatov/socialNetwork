import Vue from 'vue'
import Vuex from 'vuex'
import messagesApi from "../api/messages";
import usersApi from "../api/users";
import groupsApi from "../api/groups";

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        profile,
        messages,
        privateMessages,
        groupMessages,
        groups,
        emails,
        infoMessage,
        users,
        ...frontendData,
    },
    getters : {
        sortedMessages: state => (state.messages || []).sort((a, b) => -(a.id - b.id)),
        getPrivateByAddressee: state => id => state.privateMessages.filter(message => message.addressee.id === id || message.author.id === id).sort((a, b) => -(a.id - b.id)),
        getDialogsAndLastMessage: state => {
            let existingInterlocutor = [];
            let lastMessageInDialog = [];

            for (let i = state.privateMessages.length - 1; i >= 0; i--){
                if (existingInterlocutor.indexOf(state.privateMessages[i].addressee.id) === -1 && state.privateMessages[i].addressee.id !== state.profile.id){
                    lastMessageInDialog.push(state.privateMessages[i])
                    existingInterlocutor.push(state.privateMessages[i].addressee.id)
                }

                if (existingInterlocutor.indexOf(state.privateMessages[i].author.id) === -1 && state.privateMessages[i].author.id !== state.profile.id){
                    lastMessageInDialog.push(state.privateMessages[i])
                    existingInterlocutor.push(state.privateMessages[i].author.id)
                }

            }

            return lastMessageInDialog;
        },
        sortedGroupsWithLastMessage: state => {
            for (let i = state.groups.length - 1; i >= 0; i--){

            }
        }
    },
    mutations: {
        addMessageMutation(state, message){
            state.messages = [
                ...state.messages,
                message
            ]
        },

        addPrivateMessageMutation(state, message){
            state.privateMessages = [
                ...state.privateMessages,
                message
            ]
        },

        addGroupMessageMutation(state, message){
            state.groupMessages = [
                ...state.groupMessages,
                message
            ]
        },

        addGroupMutation(state, group){
            state.groups = [
                ...state.groups,
                group
            ]
        },

        updateMessageMutation(state, message){
            const updateIndex = state.messages.findIndex(item => item.id === message.id)
            state.messages = [
                ...state.messages.slice(0, updateIndex),
                message,
                ...state.messages.slice(updateIndex + 1)
            ]
        },

        updatePrivateMessageMutation(state, message){
            const updateIndex = state.privateMessages.findIndex(item => item.id === message.id)
            state.privateMessages = [
                ...state.privateMessages.slice(0, updateIndex),
                message,
                ...state.privateMessages.slice(updateIndex + 1)
            ]
        },

        updateGroupMessageMutation(state, message){
            const updateIndex = state.groupMessages.findIndex(item => item.id === message.id)
            state.groupMessages = [
                ...state.groupMessages.slice(0, updateIndex),
                message,
                ...state.groupMessages.slice(updateIndex + 1)
            ]
        },

        updateGroupMutation(state, group){
            const updateIndex = state.groups.findIndex(item => item.id === group.id)
            state.groups = [
                ...state.groups.slice(0, updateIndex),
                group,
                ...state.groups.slice(updateIndex + 1)
            ]
        },

        removeMessageMutation(state, message){
            const deleteIndex = state.messages.findIndex(item => item.id === message.id)

            if (deleteIndex > -1){
                state.messages = [
                    ...state.messages.slice(0, deleteIndex),
                    ...state.messages.slice(deleteIndex + 1)
                ]
            }

        },

        addMessagePageMutation(state, messages) {
            const targetMessages = state.messages
                .concat(messages)
                .reduce((res, val) => {
                    res[val.id] = val
                    return res
                }, {})

            state.messages = Object.values(targetMessages)
        },
        updateTotalPagesMutation(state, totalPages) {
            state.totalPages = totalPages
        },
        updateCurrentPageMutation(state, currentPage) {
            state.currentPage = currentPage
        },

        loadUsersMutation(state, users) {
            state.users = Object.values(users)
        },
    },
    actions:{
        async addMessageActions({commit, state}, message){
            const result = await messagesApi.addGeneral(message)
            const data = await result.json()
            const index = state.messages.findIndex(item => item.id === data.id);

            if (index > -1) {
                commit('updateMessageMutation', data)
            } else {
                commit('addMessageMutation', data)
            }
        },

        async updateMessageActions({commit}, message){
            const result = await messagesApi.update(message)
            const data = await result.json()

            commit('updateMessageMutation', data)
        },

        async removeMessageActions({commit}, message){
            const result = await messagesApi.remove(message.id)
            if (result.ok) {
                commit('removeMessageMutation', message)
            }
        },

        async addPrivateMessageActions({commit, state}, message){
            const result = await messagesApi.addPrivate(message)
            const data = await result.json()
            const index = state.privateMessages.findIndex(item => item.id === data.id);

            if (index > -1) {
                commit('updatePrivateMessageMutation', data)
            } else {
                commit('addPrivateMessageMutation', data)
            }
        },

        async addGroupMessageActions({commit, state}, message){
            const result = await messagesApi.addGroup(message)
            const data = await result.json()
            const index = state.groupMessages.findIndex(item => item.id === data.id);

            if (index > -1) {
                commit('updateGroupMessageMutation', data)
            } else {
                commit('addGroupMessageMutation', data)
            }
        },

        async addGroupActions({commit, state}, group){
            const result = await groupsApi.create(group)
            const data = await result.json()
            const index = state.groups.findIndex(item => item.id === data.id);


            if (index > -1) {
                commit('updateGroupMutation', data)
            } else {
                commit('addGroupMutation', data)
            }
        },

        async updateGroupActions({commit}, group){
            const result = await groupsApi.update(group)
            const data = await result.json()

            commit('updateGroupMutation', data)
        },

        async loadGeneralPageAction({commit, state}) {
            const response = await messagesApi.generalMessagePage(state.currentPage + 1)
            const data = await response.json()

            commit('addMessagePageMutation', data.messages)
            commit('updateTotalPagesMutation', data.totalPages)
            commit('updateCurrentPageMutation', Math.min(data.currentPage, data.totalPages - 1))
        },

        async loadUsers({commit, state}) {
            const response = await usersApi.list()
            const data = await response.json()

            commit('loadUsersMutation', data)
        },
    }
})