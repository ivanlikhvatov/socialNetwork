<template>
    <v-container fluid>
        <v-layout row>
            <v-flex xs3>
                <v-navigation-drawer
                        absolute
                        permanent
                        id="navDrawer"
                >

                    <v-dialog
                            v-model="dialog"
                            scrollable
                            max-width="500px"
                    >
                        <template v-slot:activator="{ on, attrs }">
                            <v-btn
                                    color="primary"
                                    dark
                                    v-bind="attrs"
                                    v-on="on"
                                    @click="getUsers"
                            >
                                Создать новую группу
                            </v-btn>
                        </template>
                        <v-card>
                            <v-card-title>Выберите участников</v-card-title>
                            <v-divider></v-divider>
                            <v-card-title>
                                <v-text-field :rules="rules"
                                              label="Введите название вашей группы"
                                              v-model="newGroupName"
                                ></v-text-field>
                            </v-card-title>
                            <v-card-text style="height: 700px;">
                                <v-list two-line>
                                    <v-list-item-group
                                            v-model="selected"
                                            active-class="primary--text"
                                            multiple
                                    >
                                        <template v-for="user in users" v-if="user.id !== profile.id">
                                            <v-list-item :key="user.id" @click="editSelectedMembers(user.id)">
                                                <template v-slot:default="{ active }">
                                                    <v-list-item-avatar>
                                                        <v-img v-if="user.userpic != null && user.userpic.match(/http/) != null" :src="user.userpic"></v-img>
                                                        <v-img v-else-if="user.userpic != null" :src="'/img/'+user.userpic" max-width="240px"></v-img>
                                                        <v-avatar v-else color="red">
                                                            <span class="white--text headline">{{user.name.toString()[0]}}</span>
                                                        </v-avatar>
                                                    </v-list-item-avatar>

                                                    <v-list-item-content>
                                                        <v-list-item-title v-text="user.name"></v-list-item-title>

                                                        <v-list-item-subtitle
                                                                class="text--primary"
                                                                v-text="user.email"
                                                        ></v-list-item-subtitle>

                                                        <v-list-item-subtitle v-text="user.gender"></v-list-item-subtitle>
                                                    </v-list-item-content>
                                                </template>
                                            </v-list-item>

                                            <v-divider></v-divider>
                                        </template>
                                    </v-list-item-group>
                                </v-list>
                            </v-card-text>
                            <v-divider></v-divider>
                            <v-card-actions>
                                <v-btn
                                        color="blue darken-1"
                                        text
                                        @click="dialog = false"
                                >
                                    Закрыть
                                </v-btn>
                                <v-btn
                                        color="blue darken-1"
                                        text
                                        @click="createChat"
                                >
                                    Создать
                                </v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-dialog>

                    <v-list two-line>
                        <template v-for="groupWithLastMessage in sortedGroupsWithLastMessage">
                            <v-list-item :key="groupWithLastMessage.group.id"
                                         @click="changeGroup(groupWithLastMessage.group)"
                            >
                                <template v-slot:default="{ active }">
                                    <v-list-item-content>
                                        <v-list-item-title>{{groupWithLastMessage.group.name}}</v-list-item-title>
                                        <v-list-item-subtitle v-if="groupWithLastMessage.lastMessage && groupWithLastMessage.lastMessage.author.id === profile.id">Вы: {{groupWithLastMessage.lastMessage.text}}</v-list-item-subtitle>
                                        <v-list-item-subtitle v-else-if="groupWithLastMessage.lastMessage && groupWithLastMessage.lastMessage.author.id !== profile.id">{{groupWithLastMessage.lastMessage.text}}</v-list-item-subtitle>
                                        <v-list-item-subtitle v-else>Сообщений нет</v-list-item-subtitle>
                                    </v-list-item-content>
                                </template>
                            </v-list-item>

                            <v-divider></v-divider>
                        </template>
                    </v-list>
                </v-navigation-drawer>
            </v-flex>

            <v-flex xs8 order-lg2>
                <message-form v-if="actualGroup" :messageAttr="message" :sendMessage="sendMessage"/>

                <template v-if="actualGroup">
                    <v-list v-for="groupMessage in getGroupByGroup(actualGroup.id)"
                            :key="groupMessage.id"
                            rounded
                    >
<!--                        <v-list-item-group-->
<!--                                v-model="selectMessages"-->
<!--                                color="primary"-->
<!--                        >-->
                            <v-list-item>
                                <v-list-item-avatar>
                                    <v-img v-if="groupMessage.author.userpic != null && groupMessage.author.userpic.match(/http/) != null" :src="groupMessage.author.userpic"></v-img>
                                    <v-img v-else-if="groupMessage.author.userpic != null" :src="'/img/'+groupMessage.author.userpic" max-width="240px"></v-img>
                                    <v-avatar v-else color="red">
                                        <span class="white--text headline">{{groupMessage.author.name.toString()[0]}}</span>
                                    </v-avatar>
                                </v-list-item-avatar>

                                <v-list-item-content>
                                    <v-list-item-title>
                                        {{groupMessage.text}}
                                    </v-list-item-title>

                                    <v-list-item-subtitle>
                                        <media v-if="groupMessage.link" :message="message"></media>
                                    </v-list-item-subtitle>

                                    <v-list-item-subtitle>
                                        {{new Date(groupMessage.creationDate.replace(/\s/, 'T')).getHours()}}:{{new Date(groupMessage.creationDate.replace(/\s/, 'T')).getMinutes()}}
                                    </v-list-item-subtitle>
                                </v-list-item-content>

                            </v-list-item>
<!--                        </v-list-item-group>-->

                    </v-list>
                </template>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import MessageForm from "components/messages/GroupMessageForm.vue";
    import {mapActions, mapGetters, mapState} from "vuex";
    import users from "../api/users";
    import Media from "components/media/Media.vue";

    export default {
        computed: {
            ...mapState(['profile', 'users']),
            ...mapGetters(['sortedGroupsWithLastMessage', 'getGroupByGroup'])
        },
        name: "GroupMessageList",
        // props: ['addressee'],
        data() {
            return {
                message: null,
                actualGroup: null,
                dialog: false,
                selected: [],
                selectedMembers: [],
                newGroupName: '',
                selectMessages: [],
                rules: [
                    value => !!value || 'Required.',
                    value => (value || '').length <= 20 || 'Max 20 characters'
                ],
            }
        },
        components: {
            Media,
            MessageForm
        },

        methods: {
            ...mapActions(['addGroupMessageActions', 'loadUsers', 'addGroupActions']),

            sendMessage(message){

                if (this.actualGroup){
                    message.group = this.actualGroup

                    this.addGroupMessageActions(message)
                }


            },
            changeGroup(group){
                this.actualGroup = group;
            },
            getUsers(){
                this.loadUsers()
            },
            createChat(){
                this.dialog = false;

                const group = {
                    id: '',
                    members: this.selectedMembers,
                    name: this.newGroupName
                };

                if (group.members !== null && group.members.length !== 0 && group.name !== '' && group.name !== null){
                    this.addGroupActions(group)
                }

                this.newGroupName = '';
                this.selected = [];
                this.selectedMembers = []

            },
            editSelectedMembers(userId){
                if (this.selectedMembers.indexOf(userId) !== -1){
                    this.selectedMembers.splice(this.selectedMembers.indexOf(userId), 1)
                } else {
                    this.selectedMembers.push(userId)
                }
            },
        },
    }
</script>

<style scoped>

</style>