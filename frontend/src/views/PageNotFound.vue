<template>
    <div class="bx--tile">
        <div class="bx--type-mega">404</div>
        <div>
            <p class="bx--type-beta">The page you are looking for does not exist.</p>
            <p class="bx--type-beta">Would you like to head back to
                <router-link tag="a" to="/">Squads Reports</router-link>?
            </p>
        </div>
    </div>
</template>

<script>
    export default {
        name: "PageNotFound"
    }
</script>

<style lang="scss" scoped>
    .bx--tile {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
    }
</style>
