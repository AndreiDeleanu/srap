<template>
    <div class="bx--tile"></div>
</template>

<script>
    import {mapActions} from 'vuex';

    export default {
        name: "DomainReports",
        created: function() {
            this.setContentTitle(`Reports`);
        },
        methods: {
            ...mapActions('myContent', {
                setContentTitle: 'setTitle'
            }),
        }
    }
</script>

<style lang="scss" scoped>
</style>
