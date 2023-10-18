<template>
    <div class="bx--tile">
        <!-- <MyDropdown :default-text="defaultText" :items="items"></MyDropdown> -->
        <div class="bx--grid">
            <div class="bx--row controll">
                <div class="bx--col-xs-2">
                    <dropdown2
                        default-text="Choose quarter"
                        label="Quarter"
                        :options="quartersOptions"
                        :value.sync="choosenQuarter"
                    />
                </div>
                <div class="bx--col-xs-8">
                    <label class="bx--label">Choose report level</label>
                    <div
                        data-content-switcher
                        class="bx--content-switcher"
                        role="tablist"
                        aria-label="Demo switch content"
                    >
                        <button
                            :class="{'bx--content-switcher-btn': true, 'bx--content-switcher--selected': view === 'D'}"
                            @click="() => {view = 'D'}"
                            role="tab"
                        >
                            <span>Domain</span>
                        </button>
                        <button
                            :class="{'bx--content-switcher-btn': true, 'bx--content-switcher--selected': view === 'S'}"
                            @click="() => {view = 'S'}"
                            role="tab"
                        >
                            <span>Subdomains</span>
                        </button>
                        <button
                            :class="{'bx--content-switcher-btn': true, 'bx--content-switcher--selected': view === 'Q'}"
                            @click="() => {view = 'Q'}"
                            role="tab"
                        >
                            <span>Squads</span>
                        </button>
                    </div>
                </div>
            </div>
            <report
                v-if="view === 'D' && choosenQuarter != ''"
                :override="domainReport.doAssessment"
                :overrideComment="domainReport.doComment"
                :processAssessments="domainReport.processAssessments"
                :isLoading="domainLoading"
                :projection="domainReport.projection"
                :history="domainReport.history"
            />
            <data-table
                v-if="view === 'Q' && choosenQuarter != ''"
                :headers="squadHeaders"
                :is-loading="squadsLoading"
                :rows="squadReportsRows"
                @edit-click="openQReport"
            />
            <data-table
                v-if="view === 'S' && choosenQuarter != ''"
                :headers="subdomainHeaders"
                :is-loading="subdomainsLoading"
                :rows="subdomainReportsRows"
                @edit-click="openSReport"
            />
            <report
                v-if="view === 'SR' && choosenQuarter != ''"
                :override="subdomainReport.sllOverride"
                :overrideComment="subdomainReport.sllComment"
                :processAssessments="subdomainReport.processAssessments"
                :isLoading="subdomainsLoading"
                :history="subdomainReport.history"
                :calculated="subdomainReport.calculatedValue"
            />
            <report
                v-if="view === 'QR' && choosenQuarter != ''"
                :override="squadReport.fllRating"
                :overrideComment="squadReport.fllComment"
                :processAssessments="reducedSquadReportProcesses"
                :isLoading="squadsLoading"
                :history="squadReport.history"
                :calculated="squadReport.calculatedRating"
            />
        </div>
    </div>
</template>

<script>
// import MyDropdown from '../components/MyDropdown';
import helpers from "../helpers/helpers.js";
import Dropdown2 from "../components/Dorpdown2";
import Report from "./history/report";
import DataTable from "../components/MyDataTable";
import { mapActions, mapState } from "vuex";

export default {
    name: "History",
    components: {
        // MyDropdown
        Dropdown2,
        Report,
        DataTable
    },
    computed: {
        ...mapState({
            squadsLoading: state => state.squadReport.isLoading,
            squadReports: state => state.squadReport.reports,
            subdomainsLoading: state => state.subdomainReport.isLoading,
            subdomainReports: state => state.subdomainReport.subdomainsReports,
            domainReport: state => state.domains.domainReport,
            domainLoading: state => state.domains.isLoading,
            subdomainReport: state => state.subdomainReport.subdomainReport,
            squadReport: state => state.squadReport.data,
            processes: state => state.processes.processes,
            currentThreshold: state => state.thresholdsModule.currentThreshold
        }),
        quartersOptions() {
            const quarters = helpers.getLastQuarters().map(q => {
                return { value: q, label: q };
            });
            return quarters;
        }
    },
    data() {
        return {
            choosenQuarter: "",
            view: "",
            squadHeaders: [
                { title: "Name" },
                { title: "FP Name" },
                { title: "FLL Name" },
                { title: "SLL Name" },
                { title: "Step" },
                { title: "Calculated" },
                { title: "FLL Rating" }
            ],
            subdomainHeaders: [
                { title: "Name" },
                { title: "SLL Override" },
                { title: "Calculated" },
                { title: "FLL Average" }
            ],
            squadReportsRows: [],
            subdomainReportsRows: [],
            reducedSquadReportProcesses: []
        };
    },
    watch: {
        choosenQuarter(q) {
            this.getData();
        },
        view(v) {
            this.getData();
            let suffix;
            switch (v) {
                case "D":
                    suffix = `Domain Report`;
                    break;
                case "S":
                    suffix = `Subdomain Reports`;
                    break;
                case "Q":
                    suffix = `Squad Reports`;
                    break;
                default:
                    suffix = "";
                    break;
            }
            if (suffix != "") {
                this.updateTitle(suffix);
            }
        },
        squadReports(reports) {
            this.squadReportsRows = reports.reduce((result, report) => {
                if (report.step !== "") {
                    return [
                        ...result,
                        {
                            data: [
                                report.squadName,
                                report.savedFp != ""
                                    ? report.savedFp
                                    : report.fp,
                                report.savedFll != ""
                                    ? report.savedFll
                                    : report.fll,
                                report.savedSll != ""
                                    ? report.savedSll
                                    : report.sll,
                                report.step,
                                report.calculatedRating >=
                                this.currentThreshold.satThreshold
                                    ? "Satisfactory"
                                    : report.calculatedRating >=
                                      this.currentThreshold.marginalThreshold
                                    ? "Marginal"
                                    : "Unsatisfactory",
                                report.fllRating
                            ],
                            metadata: {
                                reportId: report.reportId,
                                squadId: report.squadId
                            }
                        }
                    ];
                } else {
                    return result;
                }
            }, []);
        },
        subdomainReports(reports) {
            this.subdomainReportsRows = reports.reduce((result, sdomain) => {
                if (sdomain.sllOverride !== "") {
                    return [
                        ...result,
                        {
                            data: [
                                sdomain.subdomainName,
                                sdomain.sllOverride,
                                sdomain.calculated >=
                                this.currentThreshold.satThreshold
                                    ? "Satisfactory"
                                    : sdomain.calculated >=
                                      this.currentThreshold.marginalThreshold
                                    ? "Marginal"
                                    : "Unsatisfactory",
                                sdomain.fllAverage
                            ],
                            metadata: {
                                id: sdomain.subdomainId,
                                reportId: sdomain.subdomainReportId
                            }
                        }
                    ];
                } else {
                    return result;
                }
            }, []);
        },
        squadReport(report) {
            const processAssessments = this.processes.map(p => {
                let assesment = report.processes.find(
                    i => p.id === i.processId
                );
                if (!assesment) {
                    assesment = { comment: "", rating: 6 };
                }
                return {
                    name: p.name,
                    daComment: assesment.comment,
                    average: assesment.rating
                };
            });
            this.reducedSquadReportProcesses = processAssessments;
            this.updateTitle(report.squadName);
        },
        subdomainReport(report) {
            this.updateTitle(report.subdomainName);
        }
    },
    created: function() {
        this.setContentTitle(`History`);
        const domainId = this.$route.params.domain;
        this.$store.dispatch("processes/getAll", domainId);
    },
    methods: {
        ...mapActions("myContent", {
            setContentTitle: "setTitle"
        }),
        getData() {
            if (this.choosenQuarter === "" || this.view === "") return;

            const domainId = this.$route.params.domain;
            const quarterYearArray = this.choosenQuarter.split("Q");

            this.$store.dispatch("thresholdsModule/getThresholdsByQuarter", {
                domainId: parseInt(domainId),
                quarter: parseInt(quarterYearArray[0]),
                year: parseInt(quarterYearArray[1])
            });

            switch (this.view) {
                case "D":
                    this.$store.dispatch("domains/getDomainReportByQuarter", {
                        domainId: domainId,
                        quarter: this.choosenQuarter
                    });
                    break;
                case "S":
                    this.$store.dispatch("subdomainReport/getByQuarter", {
                        domainId: domainId,
                        quarter: this.choosenQuarter
                    });
                    break;
                case "Q":
                    this.$store.dispatch(
                        "squadReport/getReportsListByQuarter",
                        {
                            domainId: domainId,
                            quarter: this.choosenQuarter
                        }
                    );
                    break;
                default:
                    break;
            }
        },
        openQReport(data) {
            this.$store.dispatch(
                "squadReport/getSpecificReport",
                data.metadata.reportId
            );
            this.view = "QR";
        },
        openSReport(data) {
            this.$store.dispatch(
                "subdomainReport/getSpecificReport",
                data.metadata.reportId
            );
            this.view = "SR";
        },
        updateTitle(suffix) {
            this.setContentTitle(`History - ${suffix}`);
        }
    }
};
</script>

<style lang="scss" scoped>
.controll {
    margin-bottom: 1.5rem;
}
</style>
