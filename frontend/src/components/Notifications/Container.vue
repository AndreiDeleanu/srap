<template>
    <div class="notificationsContainer">
        <notification
            v-for="(n, index) in notifications"
            :key="n.time"
            :notification="n"
            :index="index"
        />
    </div>
</template>

<script>
import Notification from './Notification';
import { mapState } from 'vuex';
export default {
    name: 'Container',
    components: {
        Notification
    },
    computed: {
        ...mapState({
            notifications: state=>state.notifications.notifications
        })
    }
}
</script>

<style lang="scss" scoped>
    .notificationsContainer {
        // display: flex;
        // flex-flow: column, nowrap;
        position: fixed;
        bottom: 0;
        right: 0;
    }
</style>
