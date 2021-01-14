<template>
    <v-container>
        <v-layout align-content-space-between column>

            <message-form :messageAttr="message"/>

            <v-layout row>
                <v-flex>
                    <v-card>
                        <v-list three-line>
                            <message-row v-for="message in sortedMessages"
                                         :key="message.id"
                                         :message="message"
                                         :editMessage="editMessage"/>
                                <general-lazy-loader></general-lazy-loader>
                        </v-list>
                    </v-card>
                </v-flex>
            </v-layout>

        </v-layout>
    </v-container>
</template>

<script>
    import { mapGetters } from 'vuex'
    import MessageRow from 'components/messages/GeneralMessageRow.vue'
    import MessageForm from 'components/messages/GeneralMessageForm.vue'
    import GeneralLazyLoader from "../components/GeneralLazyLoader.vue";

    export default {
        components: {
            GeneralLazyLoader,
            MessageRow,
            MessageForm
        },
        data() {
            return {
                message: null
            }
        },
        computed: mapGetters(['sortedMessages']),
        methods: {
            editMessage(message) {
                this.message = message
            },

        }
    }
</script>

<style>
</style>