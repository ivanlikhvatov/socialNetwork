<template>
    <v-layout align-center>
        <v-text-field
                label="Write Something"
                v-model="text"
                @keyup.enter="save"
        />
        <v-btn icon @click="save">
            <v-icon>send</v-icon>
        </v-btn>
    </v-layout>
</template>

<script>
    import { mapActions } from 'vuex';


    export default {
        props: ['messageAttr'],

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
            ...mapActions(['addMessageActions', 'updateMessageActions']),
            save() {
                const message = {
                    id: this.id,
                    text : this.text
                };

                if (this.id){
                    this.updateMessageActions(message)
                } else {
                    this.addMessageActions(message)
                }
                this.text = '';
                this.id ='';
            }
        }
    }
</script>

<style>

</style>