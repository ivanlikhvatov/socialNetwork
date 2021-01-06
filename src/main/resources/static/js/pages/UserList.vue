<template>
    <v-container>
        <v-layout align-content-space-between column>
            <v-layout row>
                <v-flex>
                    <v-card>
                        <v-list two-line>
                            <template v-for="user in users">
                                <div>
                                    <v-list-item>
                                        <v-img v-if="user.userpic != null && user.userpic.match(/http/) != null" :src="user.userpic" max-width="240px" max-height="240px"></v-img>
                                        <v-img v-else-if="user.userpic != null" :src="'/img/'+user.userpic" max-width="240px" max-height="240px"></v-img>

                                        <v-avatar
                                                v-else
                                                color="red"
                                                rounded
                                                size="240"
                                                tile
                                        >
                                            <span class="white--text headline"><p style="font-size: 130px">{{user.name.toString()[0]}}</p></span>
                                        </v-avatar>

                                        <v-list two-line>
                                            <v-list-item>
                                                <v-list-item-title style="font-size: x-large" v-html="user.name"></v-list-item-title>
                                            </v-list-item>

                                            <v-list-item v-if="user.authorityType">
                                                <v-list-item-icon>
                                                    <v-icon color="indigo">
                                                        how_to_reg
                                                    </v-icon>
                                                </v-list-item-icon>

                                                <v-list-item-content>
                                                    <v-list-item-title v-html="user.authorityType"></v-list-item-title>
                                                    <v-list-item-subtitle>Тип авторизации</v-list-item-subtitle>
                                                </v-list-item-content>
                                            </v-list-item>


                                            <v-list-item v-if="user.locale">
                                                <v-list-item-icon>
                                                    <v-icon color="indigo">
                                                        location_on
                                                    </v-icon>
                                                </v-list-item-icon>

                                                <v-list-item-content>
                                                    <v-list-item-title v-html="user.locale"></v-list-item-title>
                                                    <v-list-item-subtitle>Локация</v-list-item-subtitle>
                                                </v-list-item-content>
                                            </v-list-item>

                                            <v-list-item v-if="user.email">
                                                <v-list-item-icon>
                                                    <v-icon color="indigo">
                                                        mail_outline
                                                    </v-icon>
                                                </v-list-item-icon>

                                                <v-list-item-content>
                                                    <v-list-item-title v-html="user.email"></v-list-item-title>
                                                    <v-list-item-subtitle>E-mail</v-list-item-subtitle>
                                                </v-list-item-content>
                                            </v-list-item>

                                            <v-list-item v-if="user.gender">
                                                <v-list-item-icon>
                                                    <v-icon color="indigo">
                                                        perm_identity
                                                    </v-icon>
                                                </v-list-item-icon>

                                                <v-list-item-content>
                                                    <v-list-item-title v-html="user.gender"></v-list-item-title>
                                                    <v-list-item-subtitle>Пол</v-list-item-subtitle>
                                                </v-list-item-content>
                                            </v-list-item>

                                            <v-list-item>
                                                <v-list-item-icon>
                                                    <v-icon color="indigo">
                                                        account_circle
                                                    </v-icon>
                                                </v-list-item-icon>

                                                <v-list-item-content>
                                                    <v-list-item-title>
                                                        <v-list v-for="role in user.roles">
                                                            {{role}}
                                                        </v-list>
                                                    </v-list-item-title>
                                                    <v-list-item-subtitle>Роли</v-list-item-subtitle>
                                                </v-list-item-content>
                                            </v-list-item>
                                        </v-list>
                                    </v-list-item>

                                    <v-list-item>
                                        <v-list-item-action>

                                            <v-btn
                                                    outlined
                                                    color="indigo"
                                                    @click="edit"
                                            >
                                                <v-icon left>
                                                    edit
                                                </v-icon>
                                                Редактировать профиль
                                            </v-btn>
                                        </v-list-item-action>
                                    </v-list-item>
                                </div>

                                <v-divider
                                    v-if="users[users.length - 1].id !== user.id"
                                ></v-divider>

                            </template>
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

    export default {
        name: 'UserList',
        data() {
            return {
                message: null
            }
        },
        computed: mapState(['users']),
        methods: {
            ...mapActions(['loadUsers']),
            edit(){
                // this.$router.push('/userInfo')
            }

        },

        beforeMount() {
            this.loadUsers()
        }
    }
</script>

<style>
</style>