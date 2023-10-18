<template>
    <div class="bx--form-item">
        <div data-numberinput :data-invalid="error.error || rangeError.error" class="bx--number">
            <label :for="'number-input'+_uid" class="bx--label">{{label}}</label>
            <div class="bx--number__input-wrapper">
                <input
                    :id="'number-input'+_uid"
                    type="number"
                    min="0"
                    max="100"
                    :value="value"
                    :disabled="disabled"
                    @keyup="(e) => updateValue(e.target.value)"
                >
                <div class="bx--number__controls">
                    <button
                        aria-label="increase number input"
                        class="bx--number__control-btn up-icon"
                        type="button"
                        aria-live="polite"
                        aria-atomic="true"
                        @click="increase"
                        :disabled="(limits.to != null && limits.to <= value) || disabled"
                    >
                        <svg width="10" height="5" viewBox="0 0 10 5">
                            <path d="M0 5L5 .002 10 5z" fill-rule="evenodd"></path>
                        </svg>
                    </button>
                    <button
                        aria-label="decrease number input"
                        class="bx--number__control-btn down-icon"
                        type="button"
                        aria-live="polite"
                        aria-atomic="true"
                        @click="decrease"
                        :disabled="(limits.from != null && limits.from >= value) || disabled"
                    >
                        <svg width="10" height="5" viewBox="0 0 10 5">
                            <path d="M0 0l5 4.998L10 0z" fill-rule="evenodd"></path>
                        </svg>
                    </button>
                </div>
            </div>
            <div class="bx--form-requirement" v-if="error.error">{{error.message}}</div>
            <div class="bx--form-requirement" v-if="rangeError.error">{{rangeError.message}}</div>
        </div>
    </div>
</template>

<script>
export default {
    name: "NumberImput",
    props: {
        value: {
            type: Number,
            required: true
        },
        label: {
            type: String,
            required: true
        },
        limits: {
            type: Object,
            default: () => {
                return {
                    from: null,
                    to: null
                };
            }
        },
        disabled: {
            type: Boolean,
            default: false
        },
        error: {
            type: Object,
            default: () => {
                return {
                    error: false,
                    message: ""
                };
            }
        }
    },
    data() {
        return {
            rangeError: {
                error: false,
                message: ""
            }
        };
    },
    methods: {
        updateValue(value) {
            this.$emit("input", this.validateValueRange(value));
        },
        increase() {
            if (this.limits.to == null || this.limits.to > this.value) {
                this.updateValue(this.value + 1);
            }
        },
        decrease() {
            if (this.limits.from == null || this.limits.from < this.value) {
                this.updateValue(this.value - 1);
            }
        },
        validateValueRange(value) {
            if (this.limits.to != null && value > this.limits.to) {
                this.rangeError.error = true;
                this.rangeError.message = `Maximum value is ${this.limits.to}`;
                return this.limits.to;
            } else if (this.limits.to != null && value < this.limits.from) {
                this.rangeError.error = true;
                this.rangeError.message = `Minimum value is ${
                    this.limits.from
                }`;
                return this.limits.from;
            }
            // this.rangeError.error = false;
            return value;
        }
    }
};
</script>

<style lang="scss" scoped>
.bx--number__controls {
    button:disabled {
        cursor: no-drop;
        svg {
            cursor: inherit;
            fill: grey;
        }
    }
}
</style>
