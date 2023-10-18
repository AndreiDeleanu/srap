<template>
    <div class="bx--tile">
        <div
            data-notification
            class="bx--inline-notification bx--inline-notification--warning"
            role="alert"
        >
            <div class="bx--inline-notification__details">
                <svg
                    class="bx--inline-notification__icon"
                    width="16"
                    height="16"
                    viewBox="0 0 16 16"
                    xmlns="http://www.w3.org/2000/svg"
                >
                    <path
                        d="M.75 16a.75.75 0 0 1-.67-1.085L7.33.415a.75.75 0 0 1 1.34 0l7.25 14.5A.75.75 0 0 1 15.25 16H.75zm6.5-10v5h1.5V6h-1.5zM8 13.5A.75.75 0 1 0 8 12a.75.75 0 0 0 0 1.5z"
                    ></path>
                </svg>
                <div class="bx--inline-notification__text-wrapper">
                    <p class="bx--inline-notification__title">You do not have any role asigned!</p>
                    <p
                        class="bx--inline-notification__subtitle"
                    >If you believe you are supposed to have access to this application, please contact your Domain Administrator.</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "Unauthorised"
};
</script>
