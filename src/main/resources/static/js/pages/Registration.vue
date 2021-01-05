<template>
    <v-container>
        <v-row justify="space-around">
            <v-card width="450">
                <v-card-title justify="space-around">
                    <p style="font-size: x-large; font-weight: bold; color: slategrey">Введите данные</p>
                </v-card-title>

                <v-card-text class="justify-center">
                    <form action="/registration" method="post" enctype="multipart/form-data">
                        <v-text-field
                                name="name"
                                v-model="name"
                                :error-messages="nameErrors"
                                :counter="10"
                                label="Имя"
                                required
                                @input="$v.name.$touch()"
                                @blur="$v.name.$touch()"
                        ></v-text-field>

                        <v-text-field
                                name="email"
                                v-model="email"
                                :error-messages="emailErrors"
                                label="E-mail"
                                required
                                @input="$v.email.$touch()"
                                @blur="$v.email.$touch()"
                        ></v-text-field>

                        <v-text-field
                                name="password"
                                v-model="password"
                                :error-messages="passwordErrors"
                                label="Пароль"
                                required
                                @input="$v.password.$touch()"
                                @blur="$v.password.$touch()"
                        ></v-text-field>

                        <v-select
                                name="gender"
                                v-model="select"
                                :items="items"
                                :error-messages="selectErrors"
                                label="Пол"
                                required
                                @change="$v.select.$touch()"
                                @blur="$v.select.$touch()"
                        ></v-select>

                        <v-file-input
                                type="file"
                                name="file"
                                :rules="rules"
                                accept="image/png, image/jpeg"
                                placeholder="Добавьте картинку профиля"
                                prepend-icon="add_a_photo"
                                label="Фото профиля"
                        ></v-file-input>

                        <v-btn
                                class="mr-4"
                                color="info"
                                type="submit"
                                v-if="emailErrors.length === 0 && selectErrors.length === 0 && nameErrors.length === 0 && passwordErrors.length === 0"
                        >
                            Сохранить
                        </v-btn>

                        <v-btn
                                class="mr-4"
                                color="info"
                                v-else
                        >
                            Сохранить
                        </v-btn>

                        <v-btn
                                class="mr-4"
                                color="info"
                                @click="clear"
                        >
                            Очистить
                        </v-btn>

                        <v-btn
                                class="mr-4"
                                color="info"
                                @click="showAuth"
                        >
                            Назад
                        </v-btn>
                    </form>
                </v-card-text>
            </v-card>
        </v-row>
    </v-container>
</template>

<script>
    import VueSocial from "@growthbunker/vuesocial";
    import { mapState } from 'vuex'
    import { validationMixin } from 'vuelidate'
    import { required, maxLength, email } from 'vuelidate/lib/validators'

    export default {
        name: "Registration.vue",

        mixins: [validationMixin],

        validations: {
            name: { required, maxLength: maxLength(10) },
            email: { required, email },
            select: { required },
            password: { required, maxLength: maxLength(20) }
        },

        data: () => ({
            emails: mapState['emails'],
            infoMessage: mapState['infoMessage'],
            name: '',
            email: '',
            password: '',
            select: null,
            items: [
                'муж',
                'жен',
            ],
            checkbox: false,
            rules: [
                value => !value || value.size < 10000000 || 'Avatar size should be less than 10 MB!',
            ],
        }),

        computed: {
            selectErrors () {
                const errors = []
                if (!this.$v.select.$dirty) return errors
                !this.$v.select.required && errors.push('Item is required')
                return errors
            },
            nameErrors () {
                const errors = []
                if (!this.$v.name.$dirty) return errors
                !this.$v.name.maxLength && errors.push('Name must be at most 10 characters long')
                !this.$v.name.required && errors.push('Name is required.')
                return errors
            },
            emailErrors () {
                const errors = []
                if (!this.$v.email.$dirty) return errors
                !this.$v.email.email && errors.push('Must be valid e-mail')
                !this.$v.email.required && errors.push('E-mail is required')
                if (emails.indexOf(this.email) !== -1){
                    errors.push('пользователь с таким email уже существует')
                }

                return errors
            },
            passwordErrors () {
                const errors = []
                if (!this.$v.password.$dirty) return errors
                !this.$v.password.maxLength && errors.push('Name must be at most 10 characters long')
                !this.$v.password.required && errors.push('Name is required.')
                return errors
            }
        },

        methods: {
            clear () {
                this.$v.$reset()
                this.name = ''
                this.email = ''
                this.password = ''
                this.select = null
                this.checkbox = false
            },

            showAuth(){
                this.$router.push('/auth')
            }
        },

        beforeMount() {
            if (!this.profile){
                this.$router.replace('/registration')
            }
            infoMessage = null
        }
    }
</script>

<style>

</style>