<template>
    <div class="bx--tile report-wrap">
        <div v-if="isLoading" class="bx--loading-overlay">
            <div class="bx--loading" data-loading>
                <svg class="bx--loading__svg" viewBox="-75 -75 150 150">
                    <title>Loading</title>
                    <circle cx="0" cy="0" r="37.5"></circle>
                </svg>
            </div>
        </div>
        <div v-else class="bx--grid custom-grid" id="domainRollup">
            <div class="bx--row">
                <div class="bx--col-xs-12">
                    <div class="bx--tile header top">
                        <dropdown2
                            :default-text="'Choose rating'"
                            :label="'Assessment override'"
                            :options="doAssessmentOptions"
                            :value.sync="override"
                            :disabled="true"
                        ></dropdown2>
                        <dropdown2
                            v-if="projection !== ''"
                            :default-text="'Choose rating'"
                            :label="'Assessment override'"
                            :options="doAssessmentOptions"
                            :value.sync="projection"
                            :disabled="true"
                        ></dropdown2>
                        <div class="doComment">
                            <div class="comment">
                                <label class="bx--label">{{comentLabel}}</label>
                                <div v-html="overrideComment"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="bx--row">
                <div class="bx--col-xs-12">
                    <div class="bx--tile header">
                        <span
                            class="calc-value"
                        >Calculated value: {{calculated == 0 ? calculatedValue : calculated}}%</span>
                        <div class="calc-rating">
                            <div
                                :class="'bx--tag bx--tag--' + calculatedRatingClass"
                            >{{calculatedRating}}</div>
                        </div>
                    </div>
                </div>
            </div>
            <processes :processes="rows"/>

            <timestamp :history="history"/>
        </div>
    </div>
</template>

<script>
import Dropdown2 from "../../components/Dorpdown2";
import Processes from "./processes";
import Timestamp from "../../components/Timestamp";
import { mapState } from "vuex";
import TimestampVue from "../../components/Timestamp.vue";

import RULES from "../../rules/rules";

export default {
    name: "Report",
    components: {
        Dropdown2,
        Processes,
        Timestamp
    },
    props: {
        override: {
            type: String,
            default: ""
        },
        overrideComment: {
            type: String,
            default: ""
        },
        calculated: {
            type: Number,
            default: 0
        },
        isLoading: {
            type: Boolean,
            default: true
        },
        projection: {
            type: String,
            default: ""
        },
        processAssessments: {
            type: Array,
            default: () => []
        },
        history: {
            type: Object,
            default: () => {
                return {
                    createdBy: "SRAP_SYSTEM",
                    createdTime: 1548429788206,
                    lastModifiedBy: "SRAP_SYSTEM",
                    lastModifiedTime: 1548429788206
                };
            }
        }
    },
    computed: {
        ...mapState({
            processes: state => state.processes.processes,
            currentThreshold: state => state.thresholdsModule.currentThreshold
        }),
        calculatedValue() {
            let weight = 0;
            let score = 0;
            if (this.processes != null && this.processAssessments != null) {
                this.processes.forEach(p => {
                    const r = this.processAssessments.find(
                        i =>
                            (p.id === i.id ||
                                p.name === i.name ||
                                p.name === i.processName ||
                                p.id === i.processId) &&
                            i.processId != null
                    );
                    if (r != null && r.rating != "n/a") {
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
            const val =
                this.calculated == 0 ? this.calculatedValue : this.calculated;
            if (val >= this.currentThreshold.satThreshold) {
                return "comunity";
            } else if (val >= this.currentThreshold.marginalThreshold) {
                return "private";
            } else {
                return "custom";
            }
        },
        calculatedRating() {
            const val =
                this.calculated == 0 ? this.calculatedValue : this.calculated;
            if (val >= this.currentThreshold.satThreshold) {
                return "Satisfactory";
            } else if (val >= this.currentThreshold.marginalThreshold) {
                return "Marginal";
            } else {
                return "Unsatisfactory";
            }
        },
        rows() {
            var inRow = 3;
            var row = -1;
            return this.processAssessments !== undefined
                ? this.processAssessments.reduce((r, p) => {
                      if (
                          p.processId == null &&
                          p.hasOwnProperty("processId")
                      ) {
                          return r;
                      }
                      if (inRow === 3) {
                          row = row + 1;
                          inRow = 0;
                          return [...r, [p]];
                      } else {
                          r[row].push(p);
                          inRow = inRow + 1;
                          return r;
                      }
                  }, [])
                : [];
        }
    },
    data() {
        return {
            doAssessmentOptions: [
                { label: "Satisfactory", value: "Sat" },
                { label: "Marginal", value: "Marginal" },
                { label: "Unsatisfactory", value: "Unsat" }
            ],
            comentLabel: "Override Comment"
        };
    },
    created() {}
};
</script>

<style lang="scss" scoped>
.bx--tag--community {
    background-color: #c8f08f !important;
    color: #2d660a;
}
.custom-grid {
    margin: 0 !important;
    position: relative;

    .bx--row {
        &:not(:last-of-type) {
            margin-bottom: 20px;
        }
    }

    .bx--tag {
        margin: 0 0 0.5rem auto;
        color: #152935 !important;

        &.bx--tag--custom {
            background-color: #f5a0a9;
        }
    }

    .header {
        height: auto;
        min-height: unset;
        display: flex;
        align-items: center;
        overflow: visible;

        .calc-rating {
            margin-left: 50px;

            .bx--tag {
                margin: 0 !important;
            }
        }

        &.top {
            display: flex;
            .bx--form-item {
                flex: unset;
            }
            & > div {
                margin-right: 2rem;
            }
        }
    }
}

.fll-header {
    align-items: flex-end !important;
}

.fll-comment-wrapper {
    padding-left: 5rem;

    .inner {
        display: flex;
        align-items: center;

        #fllCommentPreview {
            height: 40px;
            width: 30rem;
            background-color: #f4f7fb;
            padding: 13px 16px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;

            p {
                line-height: unset;
            }
        }

        button {
            margin-left: 0.5rem;
        }
    }
}
</style>
