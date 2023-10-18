<template>
    <div>
        <div
            :aria-describedby="uuid + '-heading'"
            :aria-labelledby="uuid + '-label'"
            :id="uuid"
            aria-modal="true"
            class="bx--modal"
            data-modal
            role="dialog"
            tabindex="-1"
        >
            <div class="bx--modal-container">
                <div class="bx--modal-header">
                    <p
                        :id="uuid + '-small-heading'"
                        class="bx--modal-header__label bx--type-delta"
                        v-if="hasSmallHeading"
                    >{{smallHeading}}</p>
                    <p
                        :id="uuid + '-heading'"
                        class="bx--modal-header__heading bx--type-beta"
                    >{{heading}}</p>
                    <button
                        @click="$emit('onCancel')"
                        aria-label="close modal"
                        class="bx--modal-close"
                        data-modal-close
                        type="button"
                        v-if="displayCloseButton"
                    >
                        <svg
                            class="bx--modal-close__icon"
                            height="10"
                            viewBox="0 0 10 10"
                            width="10"
                            xmlns="http://www.w3.org/2000/svg"
                        >
                            <title>Close Modal</title>
                            <path
                                d="M6.32 5L10 8.68 8.68 10 5 6.32 1.32 10 0 8.68 3.68 5 0 1.32 1.32 0 5 3.68 8.68 0 10 1.32 6.32 5z"
                                fill-rule="nonzero"
                            ></path>
                        </svg>
                    </button>
                </div>

                <div class="bx--modal-content">
                    <slot></slot>
                </div>

                <div class="bx--modal-footer">
                    <button
                        @click="$emit('onCancel')"
                        class="bx--btn bx--btn--secondary"
                        data-modal-close
                        type="button"
                        v-if="hasSecondaryButton"
                    >{{secondaryButtonLabel}}</button>
                    <button
                        @click="$emit('onSuccess')"
                        class="bx--btn bx--btn--primary"
                        data-modal-close
                        data-modal-primary-focus
                        type="button"
                        :disabled="disableSuccess"
                    >{{primaryButtonLabel}}</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {Modal} from "carbon-components";
    import helpers from "../helpers/helpers";

    export default {
        name: "MyModal",
        props: {
            heading: {
                type: String,
                required: true
            },
            smallHeading: {
                type: String
            },
            displayCloseButton: {
                type: Boolean,
                deafult: true
            },
            primaryButtonLabel: {
                type: String,
                required: true
            },
            secondaryButtonLabel: {
                type: String
            },
            disableSuccess: {
                type: Boolean,
                deafult: false
            }
        },
        data() {
            return {
                uuid: "data-modal-" + helpers.getRandomUuid(),
                instance: null
            }
        },
        mounted: function () {
            const modal = this.$el.querySelector("#" + this.uuid);
            this.instance = Modal.create(modal);
            const _this = this;
            modal.addEventListener('modal-beinghidden', function (e) {
                if (e.detail.launchingElement.id === _this.uuid) {
                    e.preventDefault(); // don't close modal on outside click
                }
            });
        },
        computed: {
            hasSecondaryButton: function () {
                return this.secondaryButtonLabel && this.secondaryButtonLabel.trim().length > 0
            },
            hasSmallHeading: function () {
                return this.smallHeading && this.smallHeading.trim().length > 0
            }
        },
        destroyed: function () {
            if (this.instance) {
                this.instance.release();
            }
        },
        methods: {
            show() {
                this.instance.show();
            },
            close() {
                this.instance.hide();
            }
        }
    }
</script>

<style lang="scss" scoped>
    .bx--modal-content {
        overflow: visible;
    }

    .bx--modal-container {
        min-width: 35%;
        overflow: auto;

        .bx--modal-content {
            .bx--form-item {
                // &:not(:last-of-type) {
                //     margin-bottom: 2rem;
                // }
                &.comment {
                    display: block;
                    width: 100%;
                }
            }
        }

        .bx--modal-footer {
            box-sizing: border-box;
            min-height: calc(4rem + 35px);
        }
    }

    .bx--form-item {
        margin-top: 1.7rem;
    }
</style>
