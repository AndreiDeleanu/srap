<template>
    <div class="bx--tile header">
        <span class="calc-value">Calculated value: {{calcVal}}%</span>
        <div class="calc-rating">
            <div :class="'bx--tag bx--tag--' + calculatedRatingClass">{{calculatedRating}}</div>
        </div>
    </div>
</template>

<script>
import RULES from "../../rules/rules";
import { mapState } from "vuex";

export default {
    name: "DRCalculated",
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
        }
    },
    computed: {
        ...mapState({
            currentThreshold: state => state.thresholdsModule.currentThreshold
        }),
        calcVal() {
            // const count = this.reportRatings != null ? this.reportRatings.length : 0;
            let weight = 0;
            let score = 0;
            if (this.processes != null && this.reportRatings != null) {
                this.processes.forEach(p => {
                    const r = this.reportRatings.find(
                        i => p.name === i.name || p.id == i.processId
                    );
                    if (
                        r != null &&
                        r.rating != "n/a" &&
                        r.average != null &&
                        r.average.toLowerCase() != "n/a"
                    ) {
                        weight += p.weight;
                        switch (r.average) {
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
                return "community";
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
        }
    }
};
</script>

<style lang="scss" scoped>
.calc-rating {
    margin-left: 1rem;
}
.bx--tag--comunity {
    background-color: #c8f08f;
}
</style>

