<template>
    <v-layout align-center>
        <v-text-field
                label="Write Something"
                v-model="text"
                @keyup.enter="save"
        />
        <v-btn icon @click="send">
            <v-icon>send</v-icon>
        </v-btn>
    </v-layout>
</template>

<script>
    import { mapActions } from 'vuex';


    export default {
        props: ['messageAttr', 'sendMessage'],

        data(){
            return{
                text: '',
                id: '',
            }
        },

        watch: {
            messageAttr : function(newVal, oldVal){
                this.text = newVal.text;
                this.id = newVal.id;
                this.creationDate = newVal.creationDate;
            }
        },

        methods: {
            send() {
                const message = {
                    id: this.id,
                    text : this.text,
                    addressee: null
                };

                this.sendMessage(message)

                this.text = '';
                this.id ='';
            },
        }
    }
</script>

<style>

</style>