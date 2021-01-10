<template>
    <span></span>
</template>

<script>
    import { mapActions } from 'vuex'
    export default {
        name: 'GeneralLazyLoader',
        methods: mapActions(['loadGeneralPageAction']),
        mounted() {
            window.onscroll = () => {
                const el = document.documentElement
                const isBottomOfScreen = el.scrollTop + window.innerHeight === el.offsetHeight
                if (isBottomOfScreen) {
                    this.loadGeneralPageAction()
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