<template>
    <div class="bx--tile">
        <tabs :sections="tabSections">
            <template :slot="'slot-' + tabSections[0]">
                <MyDataTable
                    :headers="headers"
                    :rows="personalSquadsData"
                    :is-loading="isLoading"
                    @edit-click="onEditClick"
                ></MyDataTable>
            </template>
            <template :slot="'slot-' + tabSections[1]">
                <MyDataTable
                    :headers="headers"
                    :rows="tableDataDelegates"
                    :is-loading="isLoading"
                    @edit-click="onEditClick"
                ></MyDataTable>
            </template>
        </tabs>
    </div>
</template>

<script>
import MyDataTable from "../components/MyDataTable";
import { Tab } from "carbon-components";
import { mapActions, mapState } from "vuex";
import { ROUTES } from "../router";

import Tabs from "../components/Tabs";

export default {
    name: "SquadsReports",
    components: {
        MyDataTable,
        Tabs
    },
    computed: {
        ...mapState({
            squads: state => state.squads.squads,
            isLoading: state => state.squads.isLoading,
            domainName: state => state.domains.domain.name,
            currentThreshold: state => state.thresholdsModule.currentThreshold
        }),
        personalSquadsData: function() {
            const user = this.$store.getters["user/getUser"];
            const rows = !!this.squads
                ? this.squads.reduce((r, squad) => {
                      if (
                          (user.roles.indexOf("DEV_ADMIN") >= 0 ||
                              user.roles.indexOf("DOMAIN_ADMIN") >= 0 ||
                              (squad.FP_name.toLowerCase() ===
                                  user.user.toLowerCase() ||
                                  squad.FLL_name.toLowerCase() ===
                                      user.user.toLowerCase() ||
                                  squad.SLL_name.toLowerCase() ===
                                      user.user.toLowerCase())) &&
                          squad.status === "Active"
                      ) {
                          return [
                              ...r,
                              {
                                  data: [
                                      squad.name,
                                      squad.FP_name,
                                      squad.FLL_name,
                                      squad.SLL_name,
                                      squad.step === ""
                                          ? "Untouched"
                                          : squad.step,
                                      squad.calculated >=
                                      this.currentThreshold.satThreshold
                                          ? "Satisfactory"
                                          : squad.calculated >=
                                            this.currentThreshold
                                                .marginalThreshold
                                          ? "Marginal"
                                          : "Unsatisfactory",
                                      squad.fllRating === ""
                                          ? "Not provided yet"
                                          : squad.fllRating
                                  ],
                                  metadata: {
                                      id: squad.id
                                  }
                              }
                          ];
                      } else {
                          return r;
                      }
                  }, [])
                : [];
            return rows;
        },
        tableDataDelegates: function() {
            const user = this.$store.getters["user/getUser"];
            const delegators = user.delegations.reduce((r, d) => {
                if (d.role === "FP" || d.role === "FLL" || d.role === "SLL")
                    return [...r, d.delegatorEmail];
                return r;
            }, []);
            const rows = !!this.squads
                ? this.squads.reduce((r, squad) => {
                      if (
                          (user.delegatedRoles.indexOf("DEV_ADMIN") >= 0 ||
                              user.delegatedRoles.indexOf("DOMAIN_ADMIN") >=
                                  0 ||
                              (delegators.includes(
                                  squad.FP_name.toLowerCase()
                              ) ||
                                  delegators.includes(
                                      squad.FLL_name.toLowerCase()
                                  ) ||
                                  delegators.includes(
                                      squad.SLL_name.toLowerCase()
                                  ))) &&
                          squad.status === "Active"
                      ) {
                          return [
                              ...r,
                              {
                                  data: [
                                      squad.name,
                                      squad.FP_name,
                                      squad.FLL_name,
                                      squad.SLL_name,
                                      squad.step === ""
                                          ? "Untouched"
                                          : squad.step,
                                      squad.calculated >=
                                      this.currentThreshold.satThreshold
                                          ? "Satisfactory"
                                          : squad.calculated >=
                                            this.currentThreshold
                                                .marginalThreshold
                                          ? "Marginal"
                                          : "Unsatisfactory",
                                      squad.fllRating === ""
                                          ? "Not provided yet"
                                          : squad.fllRating
                                  ],
                                  metadata: {
                                      id: squad.id
                                  }
                              }
                          ];
                      } else {
                          return r;
                      }
                  }, [])
                : [];
            return rows;
        }
    },
    data: () => {
        return {
            headers: [
                { title: "Name" },
                { title: "FP Name" },
                { title: "FLL Name" },
                { title: "SLL Name" },
                { title: "Step" },
                { title: "Calculated" },
                { title: "FLL Rating" }
            ],
            tabSections: ["My Squads", "Delegated Squads"]
        };
    },
    created: function() {
        const domainID = this.$route.params.domain;
        this.getAll(domainID);
        this.$store.dispatch("thresholdsModule/getCurrentThreshold", domainID);
    },
    mounted: function() {
        this.setContentTitle(`Squads Reports`);
        const tabs = this.$el.querySelector("[data-tabs]");
        Tab.create(tabs);
    },
    methods: {
        ...mapActions("squads", ["getAll"]),
        ...mapActions("myContent", {
            setContentTitle: "setTitle"
        }),
        onEditClick: function(event) {
            const path = ROUTES.SQUAD_REPORT.path.replace(
                ":id",
                event.metadata.id
            );
            this.$router.push(path);
        }
    }
};
</script>

<style lang="scss" scoped>
</style>
