import Vue from 'vue'
import Vuex from 'vuex'
import messagesApi from "../api/messages";
import usersApi from "../api/users";

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        profile,
        messages,
        privateMessages,
        emails,
        infoMessage,
        users,
        ...frontendData,
    },
    getters : {
        sortedMessages: state => (state.messages || []).sort((a, b) => -(a.id - b.id)),
        sortedPrivateMessages: state => (state.privateMessages || []).sort((a, b) => -(a.id - b.id)),

        getPrivateByAddressee: state => id => state.privateMessages.filter(message => message.addressee.id === id || message.author.id === id).sort((a, b) => -(a.id - b.id)),

        getDialogsAndLastMessage: state => {
            let existingInterlocutor = [];
            let lastMessageInDialog = [];
            // let length = state.sortedPrivateMessages().length;

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

            // alert(lastMessageInDialog)
            return lastMessageInDialog;
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
            console.log(result)
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

        async loadGeneralPageAction({commit, state}) {
            const response = await messagesApi.generalMessagePage(state.currentPage + 1)
            const data = await response.json()

            commit('addMessagePageMutation', data.messages)
            commit('updateTotalPagesMutation', data.totalPages)
            commit('updateCurrentPageMutation', Math.min(data.currentPage, data.totalPages - 1))
        },

        // async loadPrivatePageAction({commit, state}) {
        //     const response = await messagesApi.privateMessagePage()
        //     const data = await response.json()
        // },

        async loadUsers({commit, state}) {
            const response = await usersApi.list()
            const data = await response.json()

            commit('loadUsersMutation', data)
        },
    }
})