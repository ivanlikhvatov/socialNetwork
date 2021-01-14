<template>
    <v-app>
        <v-app-bar app
                   color="primary"
                   dark
        >
            <v-btn v-if="profile" icon @click="getMarginTop">
                <v-icon>dehaze</v-icon>
            </v-btn>

            <v-toolbar-title>NetLife</v-toolbar-title>

            <v-spacer></v-spacer>

            <v-btn v-if="profile" icon href="/logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-app-bar>

        <v-navigation-drawer
                v-model="drawer"
                absolute
                temporary
                id="navDrawer"
        >
            <v-list-item v-if="profile">
                <v-list-item-avatar>
                    <v-img v-if="profile.userpic != null && profile.userpic.match(/http/) != null" :src="profile.userpic"></v-img>
                    <v-img v-else-if="profile.userpic != null" :src="'/img/'+profile.userpic" max-width="240px"></v-img>
                    <v-avatar v-else color="red">
                        <span class="white--text headline">{{profile.name.toString()[0]}}</span>
                    </v-avatar>
                </v-list-item-avatar>

                <v-list-item-content>
                    <v-list-item-title>{{profile.name}}</v-list-item-title>
                </v-list-item-content>
            </v-list-item>

            <v-divider></v-divider>


            <v-btn text
                   v-if="profile"
                   :disabled="$route.path === '/profile'"
                   @click="showProfile"
            >
                <v-icon color="primary" left>perm_identity</v-icon>
                <span>Профиль</span>
            </v-btn>

            <v-btn text
                    v-if="profile"
                    :disabled="$route.path === '/'"
                    @click="showGeneralMessages"
            >
                <v-icon color="primary" left>message</v-icon>
                <span>Общий чат</span>
            </v-btn>

            <v-btn text
                   v-if="profile"
                   :disabled="$route.path === '/privateMessages'"
                   @click="showPrivateMessages"
            >
                <v-icon color="primary" left>message</v-icon>
                <span>Личные сообщения</span>
            </v-btn>

            <v-btn text
                   v-if="profile"
                   :disabled="$route.path === '/groupMessages'"
                   @click="showGroupMessages"
            >
                <v-icon color="primary" left>message</v-icon>
                <span>Групповые сообщения</span>
            </v-btn>

            <v-btn text
                   v-if="profile"
                   :disabled="$route.path === '/userList'"
                   @click="showUsers"
            >
                <v-icon color="primary" left>supervisor_account</v-icon>
                <span>Пользователи</span>
            </v-btn>

        </v-navigation-drawer>

        <v-main>
            <router-view></router-view>
        </v-main>
    </v-app>
</template>

<script>
    import { mapState, mapMutations, mapActions } from 'vuex'
    import { addHandler } from 'util/ws'

    export default {
        data: () => ({
            drawer: false,
            group: null,
        }),

        computed: mapState(['profile']),
        methods: {
            ...mapMutations(['addMessageMutation',
                'updateMessageMutation',
                'removeMessageMutation',
                'addPrivateMessageMutation',
                'updatePrivateMessageMutation',
                'addGroupMessageMutation',
                'updateGroupMessageMutation',
                'addGroupMutation',
                'updateGroupMutation']),

            showGeneralMessages(){
                this.$router.push('/')
            },
            showPrivateMessages(){
                this.$router.push('/privateMessages')
            },
            showGroupMessages(){
                this.$router.push(('/groupMessages'))
            },
            showProfile(){
                this.$router.push('/profile')
            },
            showUsers(){
                this.$router.push('/userList')
            },
            getMarginTop(){
                this.drawer = true
                const el = document.documentElement

                document.getElementById("navDrawer").style.height = window.innerHeight +"px"
                document.getElementById("navDrawer").style.marginTop = (el.scrollTop).toString() + "px"

            }
        },

        created() {
            addHandler(data => {
                if (data.objectType === 'GENERAL_MESSAGE'){
                    switch (data.eventType) {

                        case 'CREATE' :
                            this.addMessageMutation(data.body);
                            break;
                        case 'UPDATE' :
                            this.updateMessageMutation(data.body);
                            break;
                        case 'REMOVE' :
                            this.removeMessageMutation(data.body);
                            break;
                        default:
                            console.error('Looks like event type is unknown "${data.eventType}" ')
                    }
                } else if (data.objectType === 'PRIVATE_MESSAGE'){
                    switch (data.eventType) {

                        case 'CREATE' :
                            this.addPrivateMessageMutation(data.body);
                            break;
                        case 'UPDATE' :
                            this.updatePrivateMessageMutation(data.body);
                            break;
                        default:
                            console.error('Looks like event type is unknown "${data.eventType}" ')
                    }
                } else if (data.objectType === 'GROUP_MESSAGE'){
                    switch (data.eventType) {

                        case 'CREATE' :
                            this.addGroupMessageMutation(data.body);
                            break;
                        case 'UPDATE' :
                            this.updateGroupMessageMutation(data.body);
                            break;
                        default:
                            console.error('Looks like event type is unknown "${data.eventType}" ')
                    }
                } else if (data.objectType === 'GROUP'){
                    switch (data.eventType) {

                        case 'CREATE' :
                            this.addGroupMutation(data.body);
                            break;
                        case 'UPDATE' :
                            this.updateGroupMutation(data.body);
                            break;
                        default:
                            console.error('Looks like event type is unknown "${data.eventType}" ')
                    }
                } else if (data.objectType === 'LOCKED'){
                    window.location.reload()
                } else {
                    console.error('Looks like the object type is unknown "${data.bodyType}" ')
                }


            })
        },

        beforeMount() {
            if (!this.profile){
                this.$router.replace('/auth')
            }
        }
    }
</script>

<style>
</style>