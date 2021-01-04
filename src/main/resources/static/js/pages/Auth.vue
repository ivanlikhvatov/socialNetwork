<template>
    <v-container>
        Необходимо авторизоваться через:
<!--        With Facebook: <a href="/oauth2/authorization/facebook">click here</a>-->
        <a href="/oauth2/authorization/google">Google</a>
<!--        <a href="/oauth2/authorization/vk">Vk</a>-->

        <form action="/login" method="post">
            <v-text-field
                    name="email"
                    label="E-mail"
                    required

            ></v-text-field>

            <v-text-field
                    name="password"
                    label="Пароль"
                    required

            ></v-text-field>

            <v-btn
                    class="mr-4"
                    type="submit"
            >
                Войти
            </v-btn>

            <v-btn text
                   @click="showRegistrationForm"
            >
                Зарегистрироваться
            </v-btn>
        </form>

    </v-container>

</template>

<script>
    export default {
        name: "Auth.vue",
        methods:{
            showRegistrationForm(){
                this.$router.push('/registration')
            }
        }
    }
</script>

<style scoped>

</style>