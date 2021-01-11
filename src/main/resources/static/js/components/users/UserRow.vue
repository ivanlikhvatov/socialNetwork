<template>
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
                            <v-list v-for="role in user.roles" v-bind:key="role.id">
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
                <v-row>
                    <v-btn
                            v-if="profile.admin"
                            outlined
                            color="indigo"
                            @click="edit"
                    >
                        <v-icon left>
                            edit
                        </v-icon>
                        Редактировать профиль
                    </v-btn>

                    <v-btn
                            outlined
                            color="success"
                            @click="send"
                    >
                        <v-icon left>
                            message
                        </v-icon>
                        Написать
                    </v-btn>
                </v-row>
            </v-list-item-action>
        </v-list-item>

        <v-divider
                v-if="users[users.length - 1].id !== user.id"
        ></v-divider>
    </div>
</template>

<script>
    import {mapState} from "vuex";

    export default {
        name: "UserRow.vue",
        props: ['user', 'users', 'editUser'],

        computed: mapState(['profile']),

        methods: {
            edit() {
                this.editUser(this.user)
            },
            send() {

            },
        }
    }
</script>

<style scoped>

</style>