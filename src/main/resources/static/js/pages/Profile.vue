<template>
    <v-container>
        <v-card
                max-width="375"
                class="mx-auto"
        >
            <v-img v-if="profile.userpic != null && profile.userpic.match(/http/) != null" :src="profile.userpic" max-width="390px" ></v-img>
            <v-img v-else-if="profile.userpic != null" :src="'/img/'+profile.userpic" max-width="390px"></v-img>

            <v-avatar
                    v-else
                    color="red"
                    rounded
                    size="375"
                    tile
            >
                <span class="white--text headline"><p style="font-size: 130px">{{profile.name.toString()[0]}}</p></span>
            </v-avatar>

            <v-list two-line>
                <v-list-item>
                    <v-list-item-title style="font-size: x-large" v-html="profile.name"></v-list-item-title>
                </v-list-item>

                <v-divider></v-divider>

                <v-list-item v-if="profile.authorityType">
                    <v-list-item-icon>
                        <v-icon color="indigo">
                            how_to_reg
                        </v-icon>
                    </v-list-item-icon>

                    <v-list-item-content>
                        <v-list-item-title v-html="profile.authorityType"></v-list-item-title>
                        <v-list-item-subtitle>Тип авторизации</v-list-item-subtitle>
                    </v-list-item-content>
                </v-list-item>


                <v-list-item v-if="profile.locale">
                    <v-list-item-icon>
                        <v-icon color="indigo">
                            location_on
                        </v-icon>
                    </v-list-item-icon>

                    <v-list-item-content>
                        <v-list-item-title v-html="profile.locale"></v-list-item-title>
                        <v-list-item-subtitle>Локация</v-list-item-subtitle>
                    </v-list-item-content>
                </v-list-item>

                <v-list-item v-if="profile.email">
                    <v-list-item-icon>
                        <v-icon color="indigo">
                            mail_outline
                        </v-icon>
                    </v-list-item-icon>

                    <v-list-item-content>
                        <v-list-item-title v-html="profile.email"></v-list-item-title>
                        <v-list-item-subtitle>E-mail</v-list-item-subtitle>
                    </v-list-item-content>
                </v-list-item>

                <v-list-item v-if="profile.gender">
                    <v-list-item-icon>
                        <v-icon color="indigo">
                            perm_identity
                        </v-icon>
                    </v-list-item-icon>

                    <v-list-item-content>
                        <v-list-item-title v-html="profile.gender"></v-list-item-title>
                        <v-list-item-subtitle>Пол</v-list-item-subtitle>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </v-card>
    </v-container>
</template>

<script>
    import { mapState } from 'vuex'

    export default {
        name: "Profile.vue",
        computed: mapState(['profile'])
    }
</script>

<style scoped>
    img {
        max-width: 100%;
        height: auto;
    }
</style>