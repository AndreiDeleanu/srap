<template>
    <div class="bx--tile header">
        <span class="calc-value">Calculated value: {{calcVal}}%</span>
        <div class="calc-rating">
            <div :class="'bx--tag bx--tag--' + calculatedRatingClass">{{calculatedRating}}</div>
        </div>
        <div class="buttons">
            <button
                v-if="canStepBack"
                @click="openStepBackModal"
                class="bx--btn bx--btn--ghost bx--btn--sm"
            >Step Back</button>
            <button
                class="bx--btn bx--btn--primary bx--btn--sm"
                @click="$emit('save', calcVal)"
                :disabled="saveDisabled"
            >
                Save
                <svg
                    data-v-1ea2bec4
                    height="16"
                    viewBox="0 0 16 16"
                    width="16"
                    class="bx--btn__icon"
                >
                    <path
                        data-v-1ea2bec4
                        d="M12 1.597v2.406a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V1H1v14h3V9a1 1 0 0 1 1-1h6a1 1 0 0 1 1 1v6h3V4.702l-3-3.105zM1 0h10.848L16 4.298V15a1 1 0 0 1-1 1H1a1 1 0 0 1-1-1V1a1 1 0 0 1 1-1zm10 9H5v5.992l6 .007V9zM5 1v3.003h6V1H5z"
                    ></path>
                </svg>
            </button>
            <button
                class="bx--btn bx--btn--primary bx--btn--sm"
                @click="$emit('submit', calcVal)"
                :disabled="submitDisabled"
            >
                Submit
                <svg
                    data-v-1ea2bec4
                    height="16"
                    viewBox="0 0 16 16"
                    width="16"
                    class="bx--btn__icon"
                >
                    <path
                        data-v-1ea2bec4
                        d="M8 16A8 8 0 1 1 8 0a8 8 0 0 1 0 16zm3.293-11.332L6.75 9.21 4.707 7.168 3.293 8.582 6.75 12.04l5.957-5.957-1.414-1.414z"
                    ></path>
                </svg>
            </button>
        </div>
        <modal
            heading="Step Report back"
            primaryButtonLabel="Step Back"
            secondaryButtonLabel="Cancel"
            :displayCloseButton="false"
            ref="stepBackModal"
            @onSuccess="stepBackEmit"
        >
            <dropdown
                :options="options"
                :value.sync="stepTo"
                label="Choose Step to move Report to"
                defaultText="Choose Step"
            />
        </modal>
    </div>
</template>

<script>
// import helpers from '../../helpers/helpers';
import { mapState } from "vuex";
import Modal from "../../components/MyModal";
import Dropdown from "../../components/Dorpdown2";
import RULES from "../../rules/rules";

export default {
    name: "SquadCalculated",
    components: {
        Modal,
        Dropdown
    },
    props: {
        calculatedValue: {
            type: Number,
            default: 95
        },
        processes: {
            type: Array,
            required: true
        },
        reportRatings: {
            type: Array,
            required: true
        },
        submitDisabled: {
            type: Boolean,
            required: true
        },
        saveDisabled: {
            type: Boolean,
            required: true
        },
        step: {
            type: String,
            required: true
        }
    },
    computed: {
        ...mapState({
            user: state => state.user.user,
            currentThreshold: state => state.thresholdsModule.currentThreshold
        }),
        calcVal() {
            // const count = this.reportRatings != null ? this.reportRatings.length : 0;
            let weight = 0;
            let score = 0;
            if (this.processes != null && this.reportRatings != null) {
                this.processes.forEach(p => {
                    const r = this.reportRatings.find(
                        i => p.id === i.processId || p.name === i.processName
                    );
                    if (r != null && r.rating != "n/a") {
                        weight += p.weight;
                        switch (r.rating) {
                            case "Sat":
                                score += 3 * p.weight;
                                break;
                            case "Marginal":
                                score += 2 * p.weight;
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
            return parseFloat(
                Math.round((score / (weight * 3)) * 10000) / 100
            ).toFixed(2);
        },
        calculatedRatingClass() {
            if (this.calcVal >= this.currentThreshold.satThreshold) {
                return "comunity";
            } else if (
                this.calcVal >= this.currentThreshold.marginalThreshold
            ) {
                return "private";
            } else {
                return "custom";
            }
        },
        calculatedRating() {
            if (this.calcVal >= this.currentThreshold.satThreshold) {
                return "Satisfactory";
            } else if (
                this.calcVal >= this.currentThreshold.marginalThreshold
            ) {
                return "Marginal";
            } else {
                return "Unsatisfactory";
            }
        },
        roles() {
            return [...this.user.roles, ...this.user.delegatedRoles];
        },
        canStepBack() {
            return (
                (this.roles.includes("DEV_ADMIN") ||
                    this.roles.includes("DOMAIN_ADMIN")) &&
                this.step != "Draft"
            );
        },
        options() {
            const defaultOptions = ["Draft", "Submitted", "Signed"];
            const index = defaultOptions.indexOf("this.step");
            return defaultOptions.slice(0, index).map(o => {
                return {
                    label: o,
                    value: o
                };
            });
        }
    },
    data() {
        return {
            stepTo: ""
        };
    },
    methods: {
        openStepBackModal() {
            const { stepBackModal } = this.$refs;
            stepBackModal.show();
        },
        stepBackEmit() {
            this.$emit("stepBack", this.stepTo);
        }
    }
};
</script>

<style lang="scss" scoped>
.calc-rating {
    margin-left: 1rem;
}
.buttons {
    margin-left: auto;

    button {
        margin-left: 1rem;
    }
}
.bx--tag--comunity {
    background-color: #c8f08f;
}
</style>

