<template>
    <div class="bx--loading-overlay" v-if="isLoading">
        <div class="bx--loading" data-loading>
            <svg class="bx--loading__svg" viewBox="-75 -75 150 150">
                <title>Loading</title>
                <circle cx="0" cy="0" r="37.5"></circle>
            </svg>
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex';
export default {
    name: 'Spinner',
    computed: {
        ...mapState({
            isLoading: state => state.spinner.isLoading
        })
    }
}
</script>
