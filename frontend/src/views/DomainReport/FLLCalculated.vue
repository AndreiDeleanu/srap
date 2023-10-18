<template>
    <div class="bx--tile header">
        <span>FLL overrides average: {{roundedFLLValue}}%</span>
        <div :class="'bx--tag bx--tag--' + calculatedClass">{{rating}}</div>
    </div>
</template>

<script>
import RULES from "../../rules/rules";
import { mapState } from "vuex";
export default {
    name: "FLLCalculated",
    props: {
        val: {
            // type: Number,
            required: true
        }
    },
    computed: {
        ...mapState({
            currentThreshold: state => state.thresholdsModule.currentThreshold
        })
    },
    data() {
        return {
            calculatedClass: "custom",
            rating: "Unsatisfactory",
            roundedFLLValue: 0
        };
    },
    methods: {
        roundFLLAvereage() {
            this.roundedFLLValue = Math.round(this.val * 100) / 100;
        }
    },
    mounted() {
        this.roundFLLAvereage();
    },
    watch: {
        val(average) {
            this.roundFLLAvereage();
        },
        roundedFLLValue(val) {
            // console.log("val :", val);
            // if (!(val == null)) {
            //     this.calculatedClass = "custom";
            //     this.rating = "No data yet";
            // } else
            if (val >= this.currentThreshold.satThreshold) {
                this.calculatedClass = "community";
                this.rating = "Satisfactory";
            } else if (val >= this.currentThreshold.marginalThreshold) {
                this.calculatedClass = "private";
                this.rating = "Marginal";
            } else {
                this.calculatedClass = "custom";
                this.rating = "Unsatisfactory";
            }
        }
    }
};
</script>

<style lang="scss" scoped>
.header {
    span {
        margin-right: 1rem;
    }
}
</style>

