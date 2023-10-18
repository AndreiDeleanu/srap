<template>
    <div class="bx--form-item">
        <div class="dropdown">
            <div class="presented">
                <label :class="{'bx--label': true, 'bx--label--disabled': disabled}">{{label}}</label>
                <div class="trigger" :class="{'disabled': disabled}" @click="toggle">
                    {{chosenLabel != '' ? chosenLabel : defaultText}}
                    <svg
                        data-v-14682d8d
                        fill-rule="evenodd"
                        height="5"
                        viewBox="0 0 10 5"
                        width="10"
                        :class="{'bx--dropdown__arrow': true, 'open': opened}"
                    >
                        <path data-v-14682d8d d="M10 0L5 5 0 0z"></path>
                    </svg>
                </div>
            </div>
            <div class="values" v-if="opened">
                <div
                    v-for="option in options"
                    :key="option.value"
                    @click="choose(option)"
                    :class="{'disabled': option.disabled}"
                >{{option.label}}</div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Dropdown2',
    props: {
        options: {
            type: Array,
            required: true,
        },
        value: {
            type: String,
            default: ''
        },
        label: {
            type: String,
            default: ''
        },
        defaultText: {
            type: String,
            default: 'Pick a value'
        },
        disabled: {
            type: Boolean,
            default: false
        }
    },
    data() {
        return {
            opened: false,
            chosenLabel: ''
        }
    },
    watch: {
        value(value) {
            this.updateValue(value);
        }
    },
    mounted() {
        this.updateValue(this.value)
    },
    methods: {
        toggle() {
            if (this.disabled) return;
            this.opened = !this.opened;
        },
        choose(option) {
            if (option.disabled) {
                return;
            }
            this.chosenLabel = option.label;
            this.$emit('update:value', option.value);
            this.opened = false;
        },
        updateValue(value) {
            if(value === '' || value == null) {
                this.chosenLabel = this.defaultText;
            }
            for (let i = 0; i < this.options.length; i++) {
                const element = this.options[i];
                if (element.value === value) this.chosenLabel = element.label;
            }
        }
    }
}
</script>

<style lang="scss" scoped>
    .dropdown {
        display: flex;
        flex-flow: column nowrap;
        min-width: 250px;
        position: relative;

        .presented {
            .trigger {
                position: relative;
                display: block;
                background-color: #f4f7fb;
                box-shadow: 0 1px 0 0 #5a6872;
                padding: 0 1.5rem 0 1rem;
                width: 100%;
                line-height: 2.5rem;
                cursor: pointer;
                color: #152935;
                font-weight: 600;
                white-space: nowrap;
                overflow: hidden;
                font-size: 0.875rem;
                text-overflow: ellipsis;

                &.disabled {
                    cursor: no-drop;
                    color: #5a6872;
                    box-shadow: 0 1px 0 0 #b7b7b7;
                }

                svg {
                    transition: all 200ms;

                    &.open {
                        transform: rotate(-180deg);
                    }
                }
            }
        }

        .values {
            background-color: white;
            box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.1);
            position: absolute;
            bottom: 0;
            z-index: 10;
            min-width: 250px;
            transform: translateY(100%);

            div {
                display: block;
                color: currentColor;
                text-decoration: none;
                font-weight: normal;
                padding: 1rem 1.5rem 1rem 1rem;
                text-overflow: ellipsis;
                overflow: hidden;
                cursor: pointer;

                &:hover {
                    background-color: rgba(85, 150, 230, 0.1);
                    outline: 1px solid transparent;
                    text-decoration: underline;
                    color: #152935;
                }

                &.disabled {
                    color: gray;
                    cursor: no-drop;
                    &:hover {
                        background-color: white;
                        outline: 0;
                        text-decoration: none;
                        color: gray;
                    }
                }
            }
        }
    }
</style>

