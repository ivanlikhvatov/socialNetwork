<template>
    <v-container>

        <v-row>
            <v-col>
                <v-navigation-drawer
                        absolute
                        permanent
                        id="navDrawer"
                        width="370px"
                >
                    <v-list-item two-line>
                        <v-list-item-avatar>
                            <img src="https://randomuser.me/api/portraits/women/81.jpg">
                        </v-list-item-avatar>

                        <v-list-item-content>
                            <v-list-item-title>Jane Smith</v-list-item-title>
                            <v-list-item-subtitle>Logged In skf;lksd;fk;lskdf ffffffffffffffffffff</v-list-item-subtitle>
                        </v-list-item-content>
                    </v-list-item>

                    <v-divider></v-divider>

                    <v-list dense>
                        <v-list-item>

                        </v-list-item>
                    </v-list>

                </v-navigation-drawer>
            </v-col>

            <v-col>

            </v-col>
        </v-row>
    </v-container>
</template>

<script>
    export default {
        name: "PrivateMessageList"
    }
</script>

<style scoped>

</style>