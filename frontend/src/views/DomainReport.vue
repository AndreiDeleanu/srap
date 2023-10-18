<template>
    <div class="bx--tile">
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
                    <do-inputs
                        :rating.sync="domainReport.doAssessment"
                        :comment.sync="domainReport.doComment"
                        :projection.sync="domainReport.projection"
                        :showProjection="true"
                        @save="() => {save()}"
                        @calculate="calculate"
                    ></do-inputs>
                </div>
            </div>
            <div
                v-if="noReport"
                data-notification
                class="bx--inline-notification bx--inline-notification--warning"
                role="alert"
            >
                <div class="bx--inline-notification__details">
                    <svg
                        class="bx--inline-notification__icon"
                        width="16"
                        height="16"
                        viewBox="0 0 16 16"
                        xmlns="http://www.w3.org/2000/svg"
                    >
                        <path
                            d="M.75 16a.75.75 0 0 1-.67-1.085L7.33.415a.75.75 0 0 1 1.34 0l7.25 14.5A.75.75 0 0 1 15.25 16H.75zm6.5-10v5h1.5V6h-1.5zM8 13.5A.75.75 0 1 0 8 12a.75.75 0 0 0 0 1.5z"
                        ></path>
                    </svg>
                    <div class="bx--inline-notification__text-wrapper">
                        <p
                            class="bx--inline-notification__title"
                        >Report has not yet been calculated.</p>
                        <p
                            class="bx--inline-notification__subtitle"
                        >Please do so by clicking the button "Calculate from collected Data" in top right corner.</p>
                    </div>
                </div>
            </div>
            <div class="bx--row" v-if="!noReport">
                <div class="bx--col-xs-12">
                    <calculated
                        :processes="pprocesses"
                        :reportRatings="domainReport.processAssessments"
                    ></calculated>
                </div>
            </div>
            <div class="bx--row" v-if="!noReport">
                <div class="bx--col-xs-12">
                    <fll-calculated :val="domainReport.fllAverage"></fll-calculated>
                </div>
            </div>
            <processes :processes="rows" v-if="!noReport"/>
        </div>
    </div>
</template>

<script>
import { mapActions, mapState } from "vuex";

import doInputs from "./DomainReport/DO_inputs";
import calculated from "./DomainReport/calculated";
import fllCalculated from "./DomainReport/FLLCalculated";
import processes from "./DomainReport/processes";
import Spinner from "../components/Spinner";

// import helpers from '../helpers/helpers';

export default {
    name: "DomainReport",
    components: {
        doInputs,
        calculated,
        fllCalculated,
        processes,
        Spinner
    },
    computed: {
        ...mapState({
            pprocesses: state => state.processes.processes,
            domainReport: state => state.domains.domainReport,
            noReport: state => state.domains.noReport,
            isLoading: state => state.domains.isLoading
        }),
        print() {
            return [document.querySelector("#domainRollup")];
        },
        filename() {
            return "Domain rollup";
        },
        rows() {
            var inRow = 3;
            var row = -1;
            return this.domainReport.processAssessments !== undefined
                ? this.domainReport.processAssessments.reduce((r, p) => {
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
            printBox: []
        };
    },
    created: function() {
        const domainId = this.$route.params.domain;
        this.getAllProcesses(domainId);
        this.getDomainReport(domainId);
        this.$store.dispatch("thresholdsModule/getCurrentThreshold", domainId);
    },
    watch: {
        domainReport(report) {
            this.setContentTitle("Roll-Up");
        }
    },
    methods: {
        ...mapActions("myContent", {
            setContentTitle: "setTitle"
        }),
        ...mapActions("processes", {
            getAllProcesses: "getAll"
        }),
        ...mapActions("domains", {
            getDomainReport: "getDomainReport"
        }),
        save() {
            const newProcesses = this.rows.reduce(
                (r, row) => [...r, ...row.reduce((r, p) => [...r, p], [])],
                []
            );
            this.domainReport.processAssessments = newProcesses;
            this.$store.dispatch("domains/saveDomainReport", this.domainReport);
        },
        calculate() {
            this.$store.dispatch("domains/calculateDomainReport", {
                id: this.$route.params.domain,
                processAssessments: this.pprocesses
            });
        }
    }
};
</script>

<style lang="scss" scoped>
.exportButton {
    position: absolute;
    right: 0;
    top: -3rem;
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

        .buttons {
            margin-left: auto;

            button {
                &:not(:first-of-type) {
                    margin-left: 0.5rem;
                }
            }
        }
    }
}

.bx--form-item {
    &:not(:last-of-type) {
        margin-bottom: 2rem;
    }

    #comment {
        width: 100%;
        background-color: #f4f7fb;
        padding: 16px;
    }

    .footer {
        display: flex;
        justify-content: flex-end;
        width: 100%;
        margin-top: 0.5rem;

        button {
            display: flex;
            margin-left: 0.5rem;
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

