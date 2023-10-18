<template>
    <div class="bx--tile">
        <tabs :sections="tabSections">
            <template slot="slot-Overview" v-if="hasAuthorityForGraphs">
                <div class="bx--grid">
                    <div class="bx--row">
                        <div class="bx--col-md-4">
                            <PieChart
                                :data="calcCountsArray"
                                title="Calculated Ratings"
                                :color="calcRatingsColors"
                            />
                        </div>
                        <div class="bx--col-md-4">
                            <PieChart
                                :data="fllCountsArray"
                                title="FLL Overrides"
                                :color="retingsColors"
                            />
                        </div>
                        <div class="bx--col-md-4">
                            <PieChart
                                :data="progressCountsArray"
                                title="Progress of Squad Reports"
                                :color="progressColors"
                            />
                        </div>
                    </div>
                </div>
            </template>
            <template slot="slot-DataGrid">
                <div class="bx--row">
                    <div class="bx--col-xs-10">
                        <KeywordAutocomplete :keywords="suggestions" :value.sync="searchQueryArray"></KeywordAutocomplete>
                    </div>
                    <div class="bx--col-xs-2">
                        <button
                            class="bx--btn bx--btn--primary full-width"
                            type="button"
                            @click="exportToExcel"
                            :disabled="processesLoading"
                        >
                            Export to Excel
                            <svg
                                class="bx--btn__icon"
                                width="16"
                                height="16"
                                viewBox="0 0 16 16"
                            >
                                <path
                                    d="M7.5 11l4.1-4.4.7.7L7 13 1.6 7.3l.7-.7L6.5 11V0h1v11zm5.5 4v-2h1v2c0 .6-.4 1-1 1H1c-.6 0-1-.4-1-1v-2h1v2h12z"
                                ></path>
                            </svg>
                        </button>
                    </div>
                </div>
                <div class="bx--row">
                    <div class="bx--col-xs-12">
                        <data-table
                            :headers="headers"
                            :rows="rows"
                            :isLoading="loading"
                            :disableActions="true"
                            :disableSearch="true"
                            :altSearchText="searchQueryArray"
                        ></data-table>
                    </div>
                </div>
            </template>
        </tabs>
    </div>
</template>

<script>
import CircleGauge from "../components/CircleGauge";
import DataTable from "../components/MyDataTable";
import KeywordAutocomplete from "../components/KeywordAutocomplete";
import Tabs from "../components/Tabs";
import PieChart from "../components/PieChart";

import { mapActions, mapState } from "vuex";

export default {
    name: "Dashboard",
    components: {
        CircleGauge,
        DataTable,
        KeywordAutocomplete,
        Tabs,
        PieChart
    },
    computed: {
        ...mapState({
            loading: state => state.dashboardModule.loading,
            processesLoading: state => state.processes.isLoading,
            datagridData: state => state.dashboardModule.datagridData,
            user: state => state.user.user,
            calculatedLoading: state => state.dashboardModule.calculatedLoading,
            calculatedRatingCounts: state =>
                state.dashboardModule.calculatedRatingCounts,
            fllLoading: state => state.dashboardModule.fllLoading,
            fllRatingCounts: state => state.dashboardModule.fllRatingCounts,
            progressCounts: state => state.dashboardModule.progressCounts,
            progressLoading: state => state.dashboardModule.progressLoading
        }),
        rows() {
            const rows = this.datagridData.reduce((result, row) => {
                if (this.hasAccess(row)) {
                    return [
                        ...result,
                        {
                            data: [
                                row.squadName,
                                row.subdomainName,
                                row.processName,
                                row.rating,
                                row.comment
                            ]
                        }
                    ];
                }
                return result;
            }, []);
            return rows;
        },
        suggestions() {
            // console.log('this.data :', this.data);
            const suggestions = this.datagridData.reduce(
                (result, row) => {
                    if (!result.squadNames.includes(row.squadName)) {
                        result.squadNames.push(row.squadName);
                    }
                    if (!result.subdomainNames.includes(row.subdomainName)) {
                        result.subdomainNames.push(row.subdomainName);
                    }
                    if (!result.processNames.includes(row.processName)) {
                        result.processNames.push(row.processName);
                    }
                    if (
                        !result.ratings.includes(row.rating) &&
                        row.rating != ""
                    ) {
                        result.ratings.push(row.rating);
                    }
                    return result;
                },
                {
                    squadNames: [],
                    subdomainNames: [],
                    processNames: [],
                    ratings: []
                }
            );
            return suggestions;
        },
        delegators() {
            const delegators = this.user.delegations.reduce(
                (r, d) => {
                    r[d.role].push(d.name);

                    return r;
                },
                {
                    SLL: [],
                    FP: [],
                    FLL: [],
                    DOMAIN_OWNER: [],
                    SUPER_ADMIN: [],
                    DOMAIN_ADMIN: [],
                    DEV_ADMIN: []
                }
            );
            return delegators;
        },
        calcCountsArray() {
            return [
                ["Satisfactory", this.calculatedRatingCounts.satCount],
                ["Marginal", this.calculatedRatingCounts.marginalCount],
                ["Unsatisfactory", this.calculatedRatingCounts.unsatCount],
                ["Not Calculated", this.calculatedRatingCounts.notCalculated]
            ];
        },
        fllCountsArray() {
            return [
                ["Satisfactory", this.fllRatingCounts.satCount],
                ["Maringal", this.fllRatingCounts.marginalCount],
                ["Unsatisfactory", this.fllRatingCounts.unsatCount]
            ];
        },
        progressCountsArray() {
            return [
                ["Untouched", this.progressCounts.untouchedCount],
                ["Draft", this.progressCounts.draftCount],
                ["Submitted", this.progressCounts.submittedCount],
                ["Signed", this.progressCounts.signedCount]
            ];
        },
        hasAuthorityForGraphs() {
            return (
                this.user.roles.indexOf("DEV_ADMIN") >= 0 ||
                this.user.delegatedRoles.includes("DEV_ADMIN") ||
                (this.user.roles.indexOf("DOMAIN_ADMIN") >= 0 ||
                    this.user.delegatedRoles.includes("DOMAIN_ADMIN")) ||
                this.user.roles.indexOf("DOMAIN_OWNER") >= 0 ||
                this.user.delegatedRoles.includes("DOMAIN_OWNER") ||
                this.user.roles.indexOf("SLL") >= 0 ||
                this.user.delegatedRoles.includes("SLL")
                // this.user.domainId[0] === this.$route.params.domain
            );
        },
        tabSections() {
            if (this.hasAuthorityForGraphs) {
                return ["Overview", "DataGrid"];
            }
            return ["DataGrid"];
        }
    },
    data() {
        return {
            headers: [
                { title: "Squad Name" },
                { title: "Subdomain Name" },
                { title: "Process Name" },
                { title: "Rating" },
                { title: "Comment" }
            ],
            searchQueryArray: {
                squadNames: [],
                subdomainNames: [],
                processNames: [],
                ratings: []
            },
            retingsColors: ["#C8F08F", "#FDE876", "#F5A0A9"],
            calcRatingsColors: ["#C8F08F", "#FDE876", "#F5A0A9", "#6C7065"],
            progressColors: ["#47412B", "#AD7423", "#53ABB8", "#16E0C2"]
        };
    },
    created: function() {
        this.setContentTitle(`Status`);
    },
    mounted: function() {
        const { domain } = this.$route.params;
        this.$store.dispatch("dashboardModule/getCurrentQuarter", domain);
        this.$store.dispatch(
            "dashboardModule/getCalculatedRatingCounts",
            domain
        );
        this.$store.dispatch("dashboardModule/getFLLRatingCounts", domain);
        this.$store.dispatch("dashboardModule/getProgress", domain);
    },
    methods: {
        ...mapActions("myContent", {
            setContentTitle: "setTitle"
        }),
        hasAccess(row) {
            return (
                this.user.roles.indexOf("DEV_ADMIN") >= 0 ||
                this.user.delegatedRoles.includes("DEV_ADMIN") ||
                (this.user.roles.indexOf("DOMAIN_ADMIN") >= 0 ||
                    this.user.delegatedRoles.includes("DOMAIN_ADMIN")) ||
                (row.sll_email.toLowerCase() === this.user.user.toLowerCase() ||
                    this.delegators.SLL.includes(row.sll_email)) ||
                (row.fll_email.toLowerCase() === this.user.user.toLowerCase() ||
                    this.delegators.FLL.includes(row.sll_email)) ||
                (row.fp_email.toLowerCase() === this.user.user.toLowerCase() ||
                    this.delegators.FP.includes(row.fp_email))
            );
        },
        exportToExcel() {
            const parameters = {
                domainId: parseInt(this.$route.params.domain),
                ...this.searchQueryArray
            };
            // console.log("parameters :", parameters);
            this.$store.dispatch("processes/exportToExcel", parameters);
        }
    }
};
</script>

<style lang="scss" scoped>
.bx--grid {
    display: flex;
    flex-flow: column nowrap;
    margin: 0 !important;

    .bx--row {
        div[class^="bx--col-"] {
            flex-grow: 1;
            margin-bottom: 1.35rem;
        }
    }
}

.export-pdf {
    margin-top: 30px;
}

.full-width {
    width: 100%;
}
</style>
