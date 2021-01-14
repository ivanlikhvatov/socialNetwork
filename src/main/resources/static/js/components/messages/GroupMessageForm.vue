<template>
    <v-layout align-center>
        <v-text-field
                label="Введите сообщение"
                v-model="text"
                @keyup.enter="send"
        />
        <v-btn icon @click="send">
            <v-icon>send</v-icon>
        </v-btn>
    </v-layout>
</template>

<script>
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
                    group: null
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