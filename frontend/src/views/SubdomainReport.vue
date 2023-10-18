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
        <div v-else class="bx--grid custom-grid" id="subdomainRollup">
            <div class="bx--row">
                <div class="bx--col-xs-12">
                    <do-inputs
                        :rating.sync="subdomainReport.sllOverride"
                        :comment.sync="subdomainReport.sllComment"
                        @save="save"
                        @calculate="calculate"
                        @sign="sign"
                        @stepBack="stepBack"
                        :disableInputs="!reportExists && !canModify"
                        :step="subdomainReport.step"
                        :canStepBack="true"
                        ddLabel="SLL Assessment Override"
                        cLabel="SLL Comment"
                    />
                </div>
            </div>
            <template v-if="reportExists">
                <div class="bx--row">
                    <div class="bx--col-xs-12">
                        <calculated
                            :processes="processes"
                            :reportRatings="subdomainReport.processAssessments"
                        />
                    </div>
                </div>

                <div class="bx--row">
                    <div class="bx--col-xs-12">
                        <f-l-l-calculated :val="subdomainReport.fllAverage"></f-l-l-calculated>
                    </div>
                </div>

                <processes :processes="rows" :comentable="false"/>
            </template>
            <div
                v-else
            >Report has not been calculated yet. Please click the button in top right to calculate report!</div>
        </div>
    </div>
</template>

<script>
import { mapState } from "vuex";
import doInputs from "./SubdomainReport/DO_inputs";
import FLLCalculated from "./DomainReport/FLLCalculated";
import Calculated from "./DomainReport/calculated";
import processes from "./DomainReport/processes";
export default {
    name: "SubdomainReport",
    components: {
        doInputs,
        FLLCalculated,
        Calculated,
        processes
    },
    computed: {
        ...mapState({
            isLoading: state => state.subdomainReport.isLoading,
            processes: state => state.processes.processes,
            subdomainReport: state => state.subdomainReport.subdomainReport,
            domainId: state => state.user.user.domainId,
            user: state => state.user.user
        }),
        subdomainId() {
            return this.$route.params.id;
        },
        rows() {
            var inRow = 3;
            var row = -1;
            return this.subdomainReport.processAssessments !== undefined
                ? this.subdomainReport.processAssessments.reduce((r, p) => {
                      if (p.processId === null) {
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
            reportExists: false,
            canModify: false
        };
    },
    watch: {
        subdomainReport(report) {
            if (report && report.processAssessments) {
                this.reportExists = report.processAssessments.length > 0;
                this.canModify =
                    this.reportExists &&
                    (report.step !== "Signed" ||
                        (this.user.roles.indexOf("DEV_ADMIN") >= 0 ||
                            this.user.roles.indexOf("DOMAIN_ADMIN") >= 0) ||
                        (this.user.delegatedRoles.indexOf("DEV_ADMIN") >= 0 ||
                            this.user.delegatedRoles.indexOf("DOMAIN_ADMIN") >=
                                0));
            } else {
                this.reportExists = false;
                this.canModify = false;
            }

            this.$store.dispatch(
                "myContent/setTitle",
                ` - ${report.subdomainName} subdomain report`
            );
        }
    },
    created() {
        this.$store.dispatch("subdomainReport/get", this.subdomainId);
    },
    mounted() {
        this.$store.dispatch("processes/getAll", this.domainId[0]);
    },
    methods: {
        calculate() {
            const processes = this.processes.map(p => {
                return { ...p, subdomainId: this.subdomainId, processId: p.id };
            });
            console.log('processes :', processes);
            const report = {
                processAssessments: processes,
                subdomainId: this.subdomainId
            };
            if (this.subdomainReport.id) {
                report.id = this.subdomainReport.id;
            } else {
                report.id = 0;
            }
            this.$store.dispatch("subdomainReport/calculate", report);
        },
        save() {
            this.$store.dispatch("subdomainReport/save", {
                ...this.subdomainReport,
                step: "Draft"
            });
        },
        sign() {
            this.$store.dispatch("subdomainReport/save", {
                ...this.subdomainReport,
                step: "Signed"
            });
        },
        stepBack() {
            this.$store.dispatch("subdomainReport/stepBack", {
                subdomainId: this.subdomainId,
                reportId: this.subdomainReport.id,
                name: "draft"
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
