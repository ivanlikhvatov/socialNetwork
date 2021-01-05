<template>
    <v-container>
        <v-layout justify-space-around>
            <v-flex :xs6="!$vuetify.breakpoint.xsOnly">
                <div class="title mb-3">User profile</div>
                <v-layout row justify-space-between>
                    <v-flex class="px-1">
                        <v-img v-if="profile.userpic != null && profile.userpic.match(/http/) != null" :src="profile.userpic" max-width="240px"></v-img>
                        <v-img v-else-if="profile.userpic != null" :src="'/img/'+profile.userpic" max-width="240px"></v-img>
                        <v-avatar v-else color="red" size="128" tile>
                            <span class="white--text headline">{{profile.name.toString()[0]}}</span>
                        </v-avatar>
                    </v-flex>

                    <v-flex class="px-1">
                        <v-layout column>
                            <v-flex>{{profile.name}}</v-flex>
                            <v-flex>{{profile.email}}</v-flex>
                            <v-flex>{{profile.gender}}</v-flex>
                            <v-flex>{{profile.locale}}</v-flex>
                            <v-flex>{{profile.lastVisit}}</v-flex>
                        </v-layout>
                    </v-flex>
                </v-layout>
            </v-flex>
        </v-layout>
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