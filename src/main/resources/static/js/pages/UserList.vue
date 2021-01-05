<template>
    <v-container>
        <v-layout align-content-space-between column>
            <v-layout row>
                <v-flex>
                    <v-card>
                        <v-list three-line>

                            {{users.toString()}}
                            <template v-for="user in users">
                                <v-divider

                                ></v-divider>

                                <v-list-item
                                        :key="user.id"
                                >
                                    {{users}}}
                                </v-list-item>
                            </template>


<!--                            <v-list-item-group-->
<!--                                    v-for="user in users"-->
<!--                                    :key="user.id"-->
<!--                                    active-class="grey-text"-->
<!--                                    multiple-->
<!--                            >-->
<!--                            </v-list-item-group>-->
                        </v-list>
                    </v-card>
                </v-flex>
            </v-layout>

        </v-layout>
    </v-container>
</template>

<script>
    import { mapState } from 'vuex'
    import { mapActions } from 'vuex'

    export default {
        name: 'UserList',

        data() {
            return {
                message: null
            }
        },
        computed: mapState(['users']),
        methods: {
            ...mapActions(['loadUsers']),

        },

        beforeMount() {
            this.loadUsers()
            alert('ghjghj'+this.users)
        }
    }
</script>

<style>
</style>