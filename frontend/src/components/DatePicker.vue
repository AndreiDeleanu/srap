<template>
    <div class="bx--form-item">
        <div
            data-date-picker
            data-date-picker-type="single"
            class="bx--date-picker bx--date-picker--single"
            :class="{'disabled': disabled}"
            ref="datePickerWrap"
            :id="'dp'+_uid"
        >
            <div class="bx--date-picker-container">
                <label :for="id" class="bx--label">{{label}}</label>
                <svg
                    data-date-picker-icon
                    class="bx--date-picker__icon"
                    width="14"
                    height="16"
                    viewBox="0 0 14 16"
                >
                    <path
                        d="M0 5h14v1H0V5zm3-5h1v4H3V0zm7 0h1v4h-1V0zM0 2.5A1.5 1.5 0 0 1 1.5 1h11A1.5 1.5 0 0 1 14 2.5v12a1.5 1.5 0 0 1-1.5 1.5h-11A1.5 1.5 0 0 1 0 14.5v-12zm1 0v12a.5.5 0 0 0 .5.5h11a.5.5 0 0 0 .5-.5v-12a.5.5 0 0 0-.5-.5h-11a.5.5 0 0 0-.5.5z"
                        fill-rule="nonzero"
                    ></path>
                </svg>
                <input
                    :id="id"
                    :disabled="disabled"
                    :class="{'disabled': disabled}"
                    v-model="value"
                    @change="e => $emit('update:val', e.target.value)"
                    type="text"
                    class="bx--date-picker__input"
                    pattern="\d{1,2}/\d{1,2}/\d{4}"
                    placeholder="dd/mm/yyyy"
                    autocomplete="off"
                    data-date-picker-input
                >
                <div class="bx--form-requirement" v-if="!invalid">Invalid date format.</div>
            </div>
        </div>
    </div>
</template>

<script>
import { DatePicker } from 'carbon-components'
export default {
    name: 'datePicker',
    components: {
        // CalendarIcon
    },
    props: {
        id: {
            type: String,
            default: () => {return '' + Math.floor((Math.random() * 100000) + 1)}
        },
        name: {
            type: String,
            required: true
        },
        label: {
            type: String,
            required: true,
        },
        val: {
            required: true,
        },
        disabled: {
            type: Boolean,
            default: false,
        },
        invalid: {
            type: Boolean,
            default: false
        },
        required: {
            type: Boolean,
            default: false
        },
        minDate: {
            type: String,
            default: ''
        },
        maxDate: {
            type: String,
            default: ''
        }
    },
    data() {
        return {
            value: this.val,
            baseConfig: {
                dateFormat: 'd/m/Y'
            },
            dpInstance: null,
            uid: this._uid
        }
    },
    watch: {
        val(value) {
            this.value = value;
        },
        disabled: function(value) {
            if (!value) {
                this.mountDatePicker();
            } else {
                this.destroyDatePicker();
            }
        },
        minDate(nVal) {
            // console.log('this.val from minDate update :', this.val + ' uid: ' + this._uid);
            this.reInitialize();
        },
        maxDate(nVal) {
            // console.log('this.val from maxDate update :', this.val + ' uid: ' + this._uid);
            this.reInitialize();
        }
    },
    methods: {
        mountDatePicker() {
            const datePickerWrap = document.querySelector('#dp'+this._uid);
            const config = {...this.baseConfig, minDate: this.minDate, maxDate: this.maxDate};
            // console.log('this.baseConfig :', config);
            if (!this.disabled && !!datePickerWrap) {
                this.value = this.val;
                this.dpInstance = DatePicker.create(datePickerWrap, config);
            }
        },
        destroyDatePicker() {
            this.value = '';
            this.dpInstance.release();
            this.dpInstance = null;
            // this.$emit('update:val', '');
        },
        reInitialize() {
            if (!!this.dpInstance) {
                this.destroyDatePicker();
                this.mountDatePicker();
            }
        }
    },
    mounted: function() {
        this.mountDatePicker();
    },
}
</script>

<style lang="scss" scoped>
    .bx--date-picker {
        min-width: 250px;
        width: 100%;
        .bx--date-picker-container {
            width: 100%;
            input {
                max-width: unset;
                width: 100%;
            }
        }
        &.disabled {
            .bx--date-picker__icon {
                cursor: no-drop;
            }
        }
    }
</style>

