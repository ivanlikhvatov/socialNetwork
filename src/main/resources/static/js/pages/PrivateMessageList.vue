<template>
    <v-container fluid>
        <v-layout row>
            <v-flex xs3>
                <v-navigation-drawer
                        absolute
                        permanent
                        id="navDrawer"
                >
                    <v-list two-line>
                        <template v-for="message in getDialogsAndLastMessage">
                            <v-list-item :key="message.id"
                                         @click="changeDialog(message)"
                            >
                                <template v-slot:default="{ active }">
                                    <v-list-item-avatar>
                                        <v-img v-if="message.addressee.userpic != null && message.addressee.userpic.match(/http/) != null && message.addressee.id !== profile.id" :src="message.addressee.userpic"></v-img>
                                        <v-img v-else-if="message.author.userpic != null && message.author.userpic.match(/http/) != null && message.author.id !== profile.id" :src="message.author.userpic"></v-img>

                                        <v-img v-else-if="message.addressee.userpic != null && message.addressee.id !== profile.id" :src="'/img/'+message.addressee.userpic" max-width="240px"></v-img>
                                        <v-img v-else-if="message.author.userpic != null && message.author.id !== profile.id" :src="'/img/'+message.author.userpic" max-width="240px"></v-img>

                                        <v-avatar v-else color="red">
                                            <span v-if="message.addressee.id !== profile.id" class="white--text headline">{{message.addressee.name.toString()[0]}}</span>
                                            <span v-else-if="message.author.id !== profile.id" class="white--text headline">{{message.author.name.toString()[0]}}</span>
                                        </v-avatar>
                                    </v-list-item-avatar>

                                    <v-list-item-content>
                                        <v-list-item-title v-if="message.addressee.id !== profile.id">{{message.addressee.name}}</v-list-item-title>
                                        <v-list-item-title v-else>{{message.author.name}}</v-list-item-title>
                                        <v-list-item-subtitle v-if="message.author.id !== profile.id">{{message.text}}</v-list-item-subtitle>
                                        <v-list-item-subtitle v-else>Вы: {{message.text}}</v-list-item-subtitle>
                                    </v-list-item-content>
                                </template>
                            </v-list-item>

                            <v-divider></v-divider>
                        </template>
                    </v-list>
                </v-navigation-drawer>
            </v-flex>

            <v-flex xs8 order-lg2>
                <message-form v-if="actualAddressee" :messageAttr="message" :sendMessage="sendMessage"/>

                <template v-if="actualAddressee">
                    <v-list v-for="message in getPrivateByAddressee(actualAddressee.id)"
                            :key="message.id+'all'"
                    >
                        <v-list-item>
                            <v-list-item-avatar>
                                <v-img v-if="message.author.userpic != null && message.author.userpic.match(/http/) != null" :src="message.author.userpic"></v-img>
                                <v-img v-else-if="message.author.userpic != null" :src="'/img/'+message.author.userpic" max-width="240px"></v-img>
                                <v-avatar v-else color="red">
                                    <span class="white--text headline">{{message.author.name.toString()[0]}}</span>
                                </v-avatar>
                            </v-list-item-avatar>

                            <v-list-item-content>
                                <v-list-item-title>
                                    {{message.text}}
                                </v-list-item-title>

                                <v-list-item-subtitle>
                                    <media v-if="message.link" :message="message"></media>
                                </v-list-item-subtitle>

                                <v-list-item-subtitle>
                                    {{new Date(message.creationDate.replace(/\s/, 'T')).getHours()}}:{{new Date(message.creationDate.replace(/\s/, 'T')).getMinutes()}}
                                </v-list-item-subtitle>
                            </v-list-item-content>

                        </v-list-item>
                    </v-list>
                </template>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import MessageRow from "components/messages/GeneralMessageRow.vue";
    import MessageForm from "components/messages/PrivateMessageForm.vue";
    import {mapActions, mapGetters, mapState} from "vuex";
    import Media from "components/media/Media.vue";

    export default {
        computed: {
            ...mapGetters(['getPrivateByAddressee', 'getDialogsAndLastMessage']),
            ...mapState(['profile'])
        },
        name: "PrivateMessageList",
        props: ['addressee'],
        data() {
            return {
                message: null,
                actualAddressee: null
            }
        },
        components: {
            MessageRow,
            MessageForm,
            Media
        },

        methods: {
            ...mapActions(['addPrivateMessageActions']),
            editMessage(message) {
                this.message = message
            },
            sendMessage(message){

                if (this.actualAddressee){
                    message.addressee = this.actualAddressee
                }

                this.addPrivateMessageActions(message)

            },
            changeDialog(message){
                if (message.addressee.id !== this.profile.id){
                    this.actualAddressee = message.addressee
                } else if (message.author.id !== this.profile.id){
                    this.actualAddressee = message.author
                }
            }
        },

        created() {
            if (this.addressee){
                this.actualAddressee = this.addressee
            }
        }
    }
</script>

<style scoped>

</style>