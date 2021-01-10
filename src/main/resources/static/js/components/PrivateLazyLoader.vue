<template>
    <span></span>
</template>

<script>
    import {mapActions} from "vuex";

    export default {
        name: "PrivateLazyLoader",

        methods: mapActions(['loadPrivatePageAction']),
        mounted() {
            window.onscroll = () => {
                const el = document.documentElement
                const isBottomOfScreen = el.scrollTop + window.innerHeight === el.offsetHeight
                if (isBottomOfScreen) {
                    this.loadPrivatePageAction()
                }
            }
        },
        beforeDestroy() {
            window.onscroll = null
        }
    }
</script>

<style scoped>

</style>