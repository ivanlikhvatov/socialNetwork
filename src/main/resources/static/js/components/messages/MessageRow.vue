<template>

    <v-row class="my-2">
        <v-chip color="primary" class="ma-2">
            <i>({{ message.id }})</i>{{ message.text }}
            <i style="font-size: x-small">
                {{new Date(message.creationDate).getHours()}}:{{new Date(message.creationDate).getMinutes()}}
<!--                                {{message.creationDate}}-->
            </i>
        </v-chip>

        <span style="position: absolute; right: 20px">
            <v-btn icon @click="edit">
                <v-icon>edit</v-icon>
            </v-btn>

            <v-btn icon @click="del">
                <v-icon>delete_forever</v-icon>
            </v-btn>
        </span>
    </v-row>

</template>

<script>
    import { mapActions } from 'vuex'
    export default {
        props: ['message', 'editMessage'],

        methods: {
            ...mapActions(['removeMessageActions']),
            edit() {
                this.editMessage(this.message)
            },

            del() {
                console.log('REMOVE in Row');
                this.removeMessageActions(this.message)
            }
        }
    }
</script>

<style>

</style>