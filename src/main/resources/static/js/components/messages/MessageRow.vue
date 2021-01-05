<template>
    <div>
        <v-list-item>
            <v-list-item-avatar>
                <v-img v-if="message.author.userpic != null && message.author.userpic.match(/http/) != null" :src="message.author.userpic"></v-img>
                <v-img v-else-if="message.author.userpic != null" :src="'/img/'+message.author.userpic" max-width="240px"></v-img>
                <v-avatar v-else color="red">
                    <span class="white--text headline">{{message.author.name.toString()[0]}}</span>
                </v-avatar>
            </v-list-item-avatar>

            <v-list-item-content>
                <v-list-item-title v-html="authorName"></v-list-item-title>
                <v-list-item-subtitle v-html="message.text"></v-list-item-subtitle>
                <v-list-item-content>
                    <media v-if="message.link" :message="message"></media>
                </v-list-item-content>
            </v-list-item-content>

            <v-list-item-action>
                <v-list-item-action-text>
                    {{new Date(message.creationDate).getHours()}}:{{new Date(message.creationDate).getMinutes()}}
                </v-list-item-action-text>
                <div>
                    <v-btn icon @click="edit">
                        <v-icon>edit</v-icon>
                    </v-btn>

                    <v-btn icon @click="del">
                        <v-icon>delete_forever</v-icon>
                    </v-btn>
                </div>
            </v-list-item-action>
        </v-list-item>

        <v-divider></v-divider>
    </div>
</template>

<script>
    import {mapActions} from 'vuex'
    import Media from "components/media/Media.vue";

    export default {
        props: ['message', 'editMessage'],
        components: { Media },
        computed: {
            authorName() {
                return this.message.author ? this.message.author.name : 'unknown'
            }
        },
        methods: {
            ...mapActions(['removeMessageActions']),
            edit() {
                this.editMessage(this.message)
            },

            del() {
                this.removeMessageActions(this.message)
            }
        }
    }
</script>

<style>

</style>