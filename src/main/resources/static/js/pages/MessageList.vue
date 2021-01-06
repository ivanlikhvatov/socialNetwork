<template>
    <v-container>
        <v-layout align-content-space-between column>

            <message-form :messageAttr="message"/>

            <v-layout row>
                <v-flex>
                    <v-card>
                        <v-list three-line>
                            <v-list-item-group
                                    active-class="grey-text"
                                    multiple
                            >
                            <message-row v-for="message in sortedMessages"
                                         :key="message.id"
                                         :message="message"
                                         :editMessage="editMessage"/>
                                <lazy-loader></lazy-loader>
                            </v-list-item-group>
                        </v-list>
                    </v-card>
                </v-flex>
            </v-layout>

        </v-layout>
    </v-container>
</template>

<script>
    import { mapGetters } from 'vuex'
    import MessageRow from 'components/messages/MessageRow.vue'
    import MessageForm from 'components/messages/MessageForm.vue'
    import LazyLoader from "../components/LazyLoader.vue";

    export default {
        components: {
            LazyLoader,
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