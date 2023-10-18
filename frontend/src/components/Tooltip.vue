<template>
    <div class="s-tooltip-container">
        <div class="s-tooltip-trigger" @mouseover="show = true" @mouseout="show = false">
            <slot/>
        </div>
        <div class="s-tooltip" :class="{'show': show}">
            <div class="body">
                <pre>{{text}}</pre>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'tooltip',
    props: {
        text: {
            type: String,
            required: true
        }
    },
    data(){
        return {
            show: false
        }
    }
}
</script>

<style lang="scss" scoped>
    .s-tooltip-container {
        position: relative;

        .s-tooltip-trigger {
            max-width: 20rem;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            cursor: help;
        }

        .s-tooltip {
            display: none;
            position: fixed;
            margin-top: -1.5rem;
            background: #222;
            color: white;
            border: 1px solid black;
            border-radius: 6px;
            font-size: 0.8rem;
            padding: 0.5em;
            transform: translateY(-100%);
            max-width: 300px;
            white-space: pre-wrap;

            &::after {
                display: block;
                content: "";
                width: 10px;
                height: 10px;
                background: #222;
                position: absolute;
                transform: rotate(45deg);
            }

            &.show {
                display: block;
            }

            pre {
                white-space: pre-wrap;
                line-height: 1.3rem;
            }
        }
    }
</style>

