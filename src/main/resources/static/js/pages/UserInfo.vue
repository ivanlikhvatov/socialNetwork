<template>
    <v-container>
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

                <v-list v-for="role in user.roles" v-bind:key="role.id">
<!--                    <v-checkbox-->
<!--                            v-model="checkbox"-->
<!--                            :label="`Checkbox 1: ${role.toString()}`"-->
<!--                    ></v-checkbox>-->

<!--                    <v-checkbox-->
<!--                            v-model="role.toString()"-->
<!--                            label="red"-->
<!--                            color="red"-->
<!--                            value="red"-->
<!--                            hide-details-->
<!--                    ></v-checkbox>-->



<!--                    {{role}}-->
                </v-list>

                <v-divider></v-divider>

                <label><input type="checkbox" name="role">role</label>

                <v-container fluid>
                    <v-checkbox
                            v-model="checkbox1"
                            :label="`Checkbox 1: ${checkbox1.toString()}`"
                    ></v-checkbox>
                    <v-checkbox
                            v-model="checkbox2"
                            :label="`Checkbox 2: ${checkbox2.toString()}`"
                    ></v-checkbox>

                </v-container>

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

        </v-card>
    </v-container>
</template>

<script>
    export default {
        name: "UserInfo.vue",
        props: ['user'],
        data () {
            return {
                checkbox1: true,
                checkbox2: false,
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