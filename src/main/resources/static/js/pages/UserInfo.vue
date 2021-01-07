<template>
    <v-container>
        <form action="/user" method="post">
            <input type="hidden" name="userId" :value="user.id">

            <v-card
                    max-width="375"
                    class="mx-auto"
            >
                <v-img v-if="user.userpic != null && user.userpic.match(/http/) != null" :src="user.userpic" max-width="390px" ></v-img>
                <v-img v-else-if="user.userpic != null" :src="'/img/'+user.userpic" max-width="390px"></v-img>

                <v-avatar
                        v-else
                        color="red"
                        rounded
                        size="375"
                        tile
                >
                    <span class="white--text headline"><p style="font-size: 130px">{{user.name.toString()[0]}}</p></span>
                </v-avatar>

                <v-list two-line>
                    <v-list-item>
                        <v-list-item-title style="font-size: x-large" v-html="user.name"></v-list-item-title>
                    </v-list-item>

                    <v-divider></v-divider>

                    <v-list-item>
                        <v-list-item-icon>
                            <v-icon color="indigo">
                                account_circle
                            </v-icon>
                        </v-list-item-icon>

                        <v-list-item-content>
                            <v-list-item-title>
                                <label v-if="user.roles.indexOf('USER') !== -1"><input checked type="checkbox" name="USER">USER</label>
                                <label v-else><input type="checkbox" name="USER">USER</label>
                            </v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>

                    <v-list-item>
                        <v-list-item-icon>
                            <v-icon color="indigo">
                                account_circle
                            </v-icon>
                        </v-list-item-icon>

                        <v-list-item-content>
                            <v-list-item-content>
                                <label v-if="user.roles.indexOf('ADMIN') !== -1"><input checked type="checkbox" name="ADMIN">ADMIN</label>
                                <label v-else><input type="checkbox" name="ADMIN">ADMIN</label>
                            </v-list-item-content>
                        </v-list-item-content>
                    </v-list-item>

                    <v-list-item>
                        <v-list-item-content>
                            <v-switch
                                    color="red"
                                    v-model="lockedSwitch"
                                    label="Заблокировать"
                                    name="locked"
                                    :value="lockedSwitch"
                            ></v-switch>
                        </v-list-item-content>
                    </v-list-item>

                    <v-divider></v-divider>

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
                </v-list>

                <v-btn
                        class="mr-4"
                        color="info"
                        type="submit"
                >
                    Сохранить изменения
                </v-btn>

            </v-card>
        </form>

    </v-container>
</template>

<script>
    export default {
        name: "UserInfo.vue",
        props: ['user'],
        data () {
            return {
                lockedSwitch: !this.user.nonLocked,
            }
        },

        beforeMount() {
            if (this.user === undefined || this.user === null){
                this.$router.push('/userList')
            }
        }
    }
</script>

<style>
</style>