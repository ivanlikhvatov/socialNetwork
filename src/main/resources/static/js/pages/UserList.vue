<template>
    <v-container>
        <v-layout align-content-space-between column>
            <v-layout row>
                <v-flex>
                    <v-card>
                        <v-list two-line>
                            <user-row v-for="user in users"
                                      v-if="user.id !== 'bot'"
                                      :user="user"
                                      :key="user.id"
                                      :users="users"
                                      :editUser="editUser"
                            >
                            </user-row>
                        </v-list>
                    </v-card>
                </v-flex>
            </v-layout>
        </v-layout>
    </v-container>
</template>

<script>
    import { mapState } from 'vuex'
    import { mapActions } from 'vuex'
    import UserRow from 'components/messages/UserRow.vue'
    import UserInfo from 'pages/UserInfo.vue'

    export default {
        components: {
            UserRow, UserInfo
        },

        name: 'UserList',

        computed: {
            ...mapState(['users']),
        },

        methods: {
            ...mapActions(['loadUsers']),
            editUser(user){
                this.$router.push({name: 'userInfo', params: {user}})
            }
        },

        beforeMount() {
            this.loadUsers()
        }
    }
</script>

<style>
</style>