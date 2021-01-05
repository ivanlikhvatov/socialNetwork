import Vue from 'vue'
import Vuex from 'vuex'
import messagesApi from "../api/messages";

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        profile,
        messages,
        emails,
        infoMessage,
        ...frontendData,
    },
    getters : {
        sortedMessages: state => (state.messages || []).sort((a, b) => -(a.id - b.id))
    },
    mutations: {
        addMessageMutation(state, message){
            state.messages = [
                ...state.messages,
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
        }
    },
    actions:{
        async addMessageActions({commit, state}, message){
            const result = await messagesApi.add(message)
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
        async loadPageAction({commit, state}) {
            const response = await messagesApi.page(state.currentPage + 1)
            const data = await response.json()

            commit('addMessagePageMutation', data.messages)
            commit('updateTotalPagesMutation', data.totalPages)
            commit('updateCurrentPageMutation', Math.min(data.currentPage, data.totalPages - 1))
        }


    }
})