<template>
    <div class="bx--tile">
        <div v-if="isLoading || processesLoading" class="bx--loading-overlay">
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
                    <calculated
                        :processes="processes"
                        :reportRatings="report.processes"
                        :submitDisabled="!(validForSubmit && userCanSubmit)"
                        :saveDisabled="!userCanSave"
                        :step="report.status"
                        @save="save"
                        @submit="submit"
                        @stepBack="stepBackDA"
                    />
                </div>
            </div>

            <f-l-l-inputs
                v-if="report.status === 'Submitted' || report.status === 'Signed'"
                :rating.sync="report.fllRating"
                :comment.sync="report.fllComment"
                :disableInputs="!fllCan"
                ddLabel="FLL Assessment Override"
                cLabel="FLL Comment"
                @stepBack="stepBack"
                @signOff="signOff"
            />

            <processes :processes="rows" :comentable="false" @processesUpdated="updateValidation"/>

            <timestamp :history="report.history"/>
        </div>
    </div>
</template>

<script>
import { mapState } from "vuex";
import FLLInputs from "./SquadReport/FLLInputs";
import Calculated from "./SquadReport/Calculated";
import Processes from "./SquadReport/Processes";
import Timestamp from "../components/Timestamp";
export default {
    name: "SquadReport",
    components: {
        FLLInputs,
        Calculated,
        Processes,
        Timestamp
    },
    computed: {
        ...mapState({
            isLoading: state => state.squadReport.isLoading,
            report: state => state.squadReport.data,
            processes: state => state.processes.processes,
            processesLoading: state => state.processes.isLoading,
            user: state => state.user.user,
            squadName: state => state.squadReport.data.squadName
        }),
        rows() {
            var inRow = 3;
            var row = -1;
            const processes = this.processes;
            if (!!this.report.processes) {
                if (this.report.processes.length > 0) {
                    for (let i = 0; i < processes.length; i++) {
                        const process = processes[i];
                        const processAssessment = this.report.processes.find(
                            p => p.processId == process.id
                        );
                        if (processAssessment == null) {
                            this.report.processes.push({
                                processId: process.id,
                                processName: process.name,
                                rating: "",
                                comment: "",
                                description: process.description
                            });
                        } else if (process.status == "Inactive") {
                            const indexToRemove = this.report.processes
                                .map(p => p.processId)
                                .indexOf(process.id);
                            this.report.processes.splice(indexToRemove, 1);
                        }
                    }
                }
            }
            return this.report.processes !== undefined
                ? this.report.processes.reduce((r, p) => {
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
        },
        userCanSubmit() {
            const roles = [...this.user.roles, ...this.user.delegatedRoles];
            return (
                (roles.includes("DOMAIN_ADMIN") ||
                    roles.includes("DEV_ADMIN") ||
                    roles.includes("SLL") ||
                    roles.includes("FP")) &&
                (this.report.status === "Draft" || this.report.status === "")
            );
        },
        userCanSave() {
            const roles = [...this.user.roles, ...this.user.delegatedRoles];
            return (
                roles.includes("DOMAIN_ADMIN") ||
                roles.includes("DEV_ADMIN") ||
                roles.includes("SLL") ||
                (roles.includes("FP") &&
                    (this.report.status === "Draft" ||
                        this.report.status === "")) ||
                (roles.includes("FLL") && this.report.status === "Submitted")
            );
        },
        fllCan() {
            const roles = [...this.user.roles, ...this.user.delegatedRoles];
            return (
                roles.includes("DOMAIN_ADMIN") ||
                roles.includes("DEV_ADMIN") ||
                roles.includes("SLL") ||
                (roles.includes("FLL") && this.report.status === "Submitted")
            );
        }
    },
    data() {
        return {
            validForSubmit: false
        };
    },
    watch: {
        user(user) {
            if (user.domainId[0]) {
                this.getProcesses(user.domainId[0]);
                this.$store.dispatch("getCurrentThreshold", user.domainId[0]);
            }
        },
        squadName(name) {
            this.updateTitle(name);
        }
    },
    methods: {
        getProcesses(domainId) {
            this.$store.dispatch("processes/getAll", domainId);
        },
        save(calculatedRating) {
            this.report.calculatedRating = calculatedRating;
            this.report.processes = this.rows.reduce(
                (r, row) => [...r, ...row.reduce((res, p) => [...res, p], [])],
                []
            );
            this.report.squadId = this.$route.params.id; // set report id to squad id
            this.$store.dispatch("squadReport/save", this.report);
        },
        submit(calculatedRating) {
            this.report.calculatedRating = calculatedRating;
            this.report.processes = this.rows.reduce(
                (r, row) => [...r, ...row.reduce((res, p) => [...res, p], [])],
                []
            );
            this.report.squadId = this.$route.params.id; // set report id to squad id
            this.report.status = "Submitted";
            this.$store.dispatch("squadReport/submit", this.report);
        },
        stepBack() {
            this.$store.dispatch("squadReport/squadStepBackToFP", {
                reportId: this.report.reportId,
                reportName: this.report.squadName,
                recipients: this.report.fpName
            });
        },
        signOff() {
            this.report.squadId = this.$route.params.id; // set report id to squad id
            this.report.status = "Signed";
            this.$store.dispatch("squadReport/submit", this.report);
        },
        updateValidation() {
            let valid = true;
            if (this.rows) {
                const rows = this.rows.reduce(
                    (r, row) => [
                        ...r,
                        ...row.reduce((res, p) => [...res, p], [])
                    ],
                    []
                );
                for (let i = 0; i < rows.length; i++) {
                    if (
                        rows[i].rating == null ||
                        (rows[i].rating !== "Sat" && rows[i].comment === "")
                    ) {
                        valid = false;
                        break;
                    }
                }
            }
            this.validForSubmit = valid;
        },
        stepBackDA(step) {
            const recipient =
                step == "Draft" ? this.report.fpName : this.report.fllName;
            this.$store.dispatch("squadReport/stepBack", {
                step: step,
                reportId: this.report.reportId,
                reportName: this.report.squadName,
                recipients: recipient
            });
        },
        updateTitle(title) {
            this.$store.dispatch(
                "myContent/setTitle",
                ` - ${title} squad Assessment`
            );
        }
    },
    created() {
        this.$store.dispatch("squadReport/get", this.$route.params.id);
        if (this.user && this.user.domainId) {
            this.getProcesses(this.user.domainId[0]);
            this.$store.dispatch(
                "thresholdsModule/getCurrentThreshold",
                this.user.domainId[0]
            );
        }
    },
    mounted() {
        this.updateValidation();
        this.updateTitle(this.report.squadName);
    }
};
</script>

<style lang="scss" scoped>
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
