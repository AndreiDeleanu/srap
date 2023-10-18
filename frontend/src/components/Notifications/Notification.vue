<template>
    <div
        data-notification
        :class="'bx--toast-notification bx--toast-notification--' + notification.severity"
        role="alert"
    >
        <div class="bx--toast-notification__details">
            <h3 class="bx--toast-notification__title">{{notification.title}}</h3>
            <p class="bx--toast-notification__subtitle">{{notification.subtitle}}</p>
            <p class="bx--toast-notification__caption">{{notification.caption}}</p>
        </div>
        <button
            data-notification-btn
            class="bx--toast-notification__close-button"
            type="button"
            aria-label="close"
            @click="remove"
        >
            <svg
                aria-hidden="true"
                class="bx--toast-notification__close-icon"
                width="10"
                height="10"
                viewBox="0 0 10 10"
                xmlns="http://www.w3.org/2000/svg"
            >
                <path
                    d="M6.32 5L10 8.68 8.68 10 5 6.32 1.32 10 0 8.68 3.68 5 0 1.32 1.32 0 5 3.68 8.68 0 10 1.32 6.32 5z"
                    fill-rule="nonzero"
                ></path>
            </svg>
        </button>
    </div>
</template>

<script>
export default {
    name: 'Notification',
    props: {
        /**
         * {title: String,
            subtitle: String,
            caption: Strgin,
            severity}
         */
        notification: {
            type: Object,
            required: true
        },
        index: {
            type: Number,
            required: true
        }
    },
    methods: {
        remove: function() {
            this.$store.dispatch('notifications/removeNotification', this.index);
        }
    }
}
</script>
