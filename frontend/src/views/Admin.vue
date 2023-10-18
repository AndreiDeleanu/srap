<template>
    <div class="bx--tile">
        <nav data-tabs class="bx--tabs" role="navigation">
            <div class="bx--tabs-trigger" tabindex="0">
                <a href="javascript:void(0)" class="bx--tabs-trigger-text" tabindex="-1"></a>
                <svg width="10" height="5" viewBox="0 0 10 5">
                    <path d="M0 0l5 4.998L10 0z" fill-rule="evenodd"></path>
                </svg>
            </div>
            <ul class="bx--tabs__nav bx--tabs__nav--hidden" role="tablist">
                <li
                    class="bx--tabs__nav-item bx--tabs__nav-item--selected"
                    data-target=".tab-1"
                    role="presentation"
                    v-if="!isSuperAdmin || isDev || isDA"
                >
                    <a
                        id="tab-link-1"
                        class="bx--tabs__nav-link"
                        href="javascript:void(0)"
                        role="tab"
                        aria-controls="tab-panel-1"
                        aria-selected="true"
                    >Processes</a>
                </li>
                <li
                    class="bx--tabs__nav-item"
                    data-target=".tab-2"
                    role="presentation"
                    v-if="!isSuperAdmin || isDev || isDA"
                >
                    <a
                        id="tab-link-2"
                        class="bx--tabs__nav-link"
                        href="javascript:void(0)"
                        role="tab"
                        aria-controls="tab-panel-2"
                    >Subdomains</a>
                </li>
                <li
                    class="bx--tabs__nav-item"
                    data-target=".tab-3"
                    role="presentation"
                    v-if="!isSuperAdmin || isDev || isDA"
                >
                    <a
                        id="tab-link-3"
                        class="bx--tabs__nav-link"
                        href="javascript:void(0)"
                        role="tab"
                        aria-controls="tab-panel-3"
                    >Squads</a>
                </li>
                <li
                    class="bx--tabs__nav-item"
                    data-target=".tab-5"
                    role="presentation"
                    v-if="!isSuperAdmin || isDev || isDA"
                >
                    <a
                        id="tab-link-5"
                        class="bx--tabs__nav-link"
                        href="javascript:void(0)"
                        role="tab"
                        aria-controls="tab-panel-5"
                    >Deadlines</a>
                </li>

                <li
                    class="bx--tabs__nav-item"
                    data-target=".tab-6"
                    role="presentation"
                    v-if="!isSuperAdmin || isDev || isDA"
                >
                    <a
                        id="tab-link-6"
                        class="bx--tabs__nav-link"
                        href="javascript:void(0)"
                        role="tab"
                        aria-controls="tab-panel-6"
                    >Thresholds</a>
                </li>
                <li
                    class="bx--tabs__nav-item"
                    data-target=".tab-4"
                    role="presentation"
                    v-if="isSuperAdmin || isDev"
                >
                    <a
                        id="tab-link-4"
                        class="bx--tabs__nav-link"
                        href="javascript:void(0)"
                        role="tab"
                        aria-controls="tab-panel-4"
                    >Domains</a>
                </li>
            </ul>
        </nav>
        <div
            id="tab-panel-1"
            class="tab-1"
            role="tabpanel"
            aria-labelledby="tab-link-1"
            :aria-hidden="isSuperAdmin || !isDev"
            v-bind="firstOptions"
        >
            <my-modal
                heading="Edit Process"
                primary-button-label="Save Process"
                secondary-button-label="Cancel"
                ref="editProcessModal"
                @onSuccess="updateProcess"
            >
                <div class="bx--form-item">
                    <label for="squad_name" class="bx--label">Process Name</label>
                    <input
                        id="squad_name"
                        type="text"
                        class="bx--text-input"
                        v-model="updatedProcess.name"
                    >
                </div>
                <div class="bx--form-item">
                    <label for="squad_name" class="bx--label">Process Description</label>
                    <input
                        id="squad_name"
                        type="text"
                        class="bx--text-input"
                        v-model="updatedProcess.description"
                    >
                </div>
                <div class="bx--form-item">
                    <label for="squad_name" class="bx--label">Process Weight</label>
                    <input
                        id="squad_name"
                        type="number"
                        class="bx--text-input"
                        min="1"
                        max="10"
                        maxlength="2"
                        v-model="updatedProcess.weight"
                    >
                </div>
            </my-modal>
            <MyDataTable
                id="test-table"
                :headers="tableData.headers"
                :rows="tableData.rows"
                :add-button-label="'New Process'"
                :is-loading="isLoading"
                :canDeactivate="true"
                @edit-click="onEditClick"
                @deactivate-click="onDeactivateClick"
            >
                <button
                    class="bx--btn bx--btn--primary bx--btn--sm"
                    type="button"
                    @click="openProcessModal"
                >
                    Add Process
                    <svg
                        class="bx--btn__icon"
                        width="16"
                        height="16"
                        viewBox="0 0 16 16"
                        xmlns="http://www.w3.org/2000/svg"
                    >
                        <path
                            d="M7 7H4v2h3v3h2V9h3V7H9V4H7v3zm1 9A8 8 0 1 1 8 0a8 8 0 0 1 0 16z"
                            fill-rule="evenodd"
                        ></path>
                    </svg>
                </button>
                <my-modal
                    heading="Add new Process"
                    primary-button-label="Save Process"
                    secondary-button-label="Cancel"
                    ref="processModal"
                    @onSuccess="submitProcess"
                >
                    <div class="bx--form-item">
                        <label for="squad_name" class="bx--label">Process Name</label>
                        <input
                            id="squad_name"
                            type="text"
                            class="bx--text-input"
                            v-model="newProcess.name"
                        >
                    </div>
                    <div class="bx--form-item">
                        <label for="squad_name" class="bx--label">Process Description</label>
                        <input
                            id="squad_name"
                            type="text"
                            class="bx--text-input"
                            v-model="newProcess.description"
                        >
                    </div>
                    <div class="bx--form-item">
                        <label for="squad_name" class="bx--label">Process Weight</label>
                        <input
                            id="squad_name"
                            type="number"
                            class="bx--text-input"
                            min="1"
                            max="10"
                            maxlength="2"
                            v-model="newProcess.weight"
                        >
                    </div>
                </my-modal>
            </MyDataTable>
        </div>
        <div
            id="tab-panel-2"
            class="tab-2"
            role="tabpanel"
            aria-labelledby="tab-link-2"
            aria-hidden="true"
            hidden
        >
            <admin-subdomains/>
        </div>
        <div
            id="tab-panel-3"
            class="tab-3"
            role="tabpanel"
            aria-labelledby="tab-link-3"
            aria-hidden="true"
            hidden
        >
            <admin-squads/>
        </div>
        <div
            id="tab-panel-4"
            class="tab-4"
            role="tabpanel"
            aria-labelledby="tab-link-4"
            :aria-hidden="!isSuperAdmin || isDev"
            v-bind="lastOptions"
        >
            <domains/>
        </div>
        <div
            id="tab-panel-5"
            class="tab-5"
            role="tabpanel"
            aria-labelledby="tab-link-5"
            aria-hidden="true"
            hidden
        >
            <deadlines/>
        </div>
        <div
            id="tab-panel-6"
            class="tab-6"
            role="tabpanel"
            aria-labelledby="tab-link-5"
            aria-hidden="true"
            hidden
        >
            <thresholds/>
        </div>
    </div>
</template>

<script>
import MyDataTable from "../components/MyDataTable";
import AdminSubdomains from "./Admin/AdminSubdomains";
import AdminSquads from "./Admin/AdminSquads";
import Domains from "./Admin/Domains.vue";
import MyModal from "../components/MyModal";
import Deadlines from "./Admin/Deadlines";
import Thresholds from "./Admin/AdminThresholds";

import { Tab } from "carbon-components";
import { mapActions, mapState } from "vuex";

export default {
    name: "Admin",
    components: {
        MyDataTable,
        AdminSubdomains,
        AdminSquads,
        Domains,
        MyModal,
        Deadlines,
        Thresholds
    },
    computed: {
        ...mapState({
            processes: state => state.processes.processes,
            isLoading: state => state.processes.isLoading,
            domainId: state => state.user.user.domainId[0]
            // roles: state=>state.user.roles
        }),
        tableData: function() {
            const headers = [
                { title: "Name" },
                { title: "Description" },
                { title: "Weight", isNum: true },
                { title: "Active" }
            ];
            const rows = this.processes.map(process => {
                return {
                    data: [
                        process.name,
                        process.description,
                        process.weight,
                        process.status
                    ],
                    metadata: {
                        id: process.id,
                        status: process.status
                    }
                };
            });

            return { headers, rows };
        },
        isSuperAdmin: function() {
            const user = this.$store.getters["user/getUser"];
            if (user && user.roles) {
                return (
                    user.roles.includes("SUPER_ADMIN") ||
                    user.delegatedRoles.includes("SUPER_ADMIN")
                );
            }
            return false;
        },
        isDev: function() {
            const user = this.$store.getters["user/getUser"];
            if (user && user.roles) {
                return (
                    user.roles.includes("DEV_ADMIN") ||
                    user.delegatedRoles.includes("DEV_ADMIN")
                );
            }
            return false;
        },
        isDA() {
            const user = this.$store.getters["user/getUser"];
            if (user && user.roles) {
                return (
                    user.roles.includes("DOMAIN_ADMIN") ||
                    user.delegatedRoles.includes("DOMAIN_ADMIN")
                );
            }
            return false;
        },
        firstOptions: function() {
            if (this.isDev || this.isDA) return {};
            if (this.isSuperAdmin) return { [`hidden`]: true };
        },
        lastOptions: function() {
            if (!this.isSuperAdmin || this.isDA) return { [`hidden`]: true };
        }
    },
    data() {
        return {
            newProcess: {
                name: "",
                description: "",
                domainId: -1,
                status: 6,
                weight: 1
            },
            updatedProcess: {
                id: -1,
                name: "",
                description: "",
                domainId: -1,
                status: 6,
                weight: 1
            }
        };
    },
    created() {
        this.setContentTitle(`Administration`);
        this.getAllProcesses(this.$route.params.domain);
        this.$store.dispatch("subdomains/getAll", this.$route.params.domain);
        this.$store.dispatch(
            "thresholdsModule/getAllThresholds",
            this.$route.params.domain
        );
    },
    mounted() {
        const tabs = this.$el.querySelector("[data-tabs]");
        Tab.create(tabs);
    },
    methods: {
        ...mapActions("processes", {
            getAllProcesses: "getAll"
        }),
        ...mapActions("myContent", {
            setContentTitle: "setTitle"
        }),
        onEditClick(process) {
            this.updatedProcess.id = process.metadata.id;
            this.updatedProcess.name = process.data[0];
            this.updatedProcess.description = process.data[1];
            this.updatedProcess.weight = process.data[2];
            this.updatedProcess.status = "Active";
            this.updatedProcess.domainId = this.$route.params.domain;
            const { editProcessModal } = this.$refs;
            editProcessModal.show();
        },
        onDeactivateClick(process) {
            this.updatedProcess.id = process.metadata.id;
            this.updatedProcess.name = process.data[0];
            this.updatedProcess.description = process.data[1];
            this.updatedProcess.weight = process.data[2];
            this.updatedProcess.domainId = this.$route.params.domain;
            this.updatedProcess.status = "Inactive";
            this.$store.dispatch(
                "processes/updateProcess",
                this.updatedProcess
            );
        },
        openProcessModal() {
            const { processModal } = this.$refs;
            processModal.show();
        },
        submitProcess() {
            const newProcess = this.newProcess;
            newProcess.domainId = parseInt(this.$route.params.domain);
            this.$store.dispatch("processes/createProcess", newProcess);
        },
        updateProcess() {
            this.$store.dispatch(
                "processes/updateProcess",
                this.updatedProcess
            );
        }
    }
};
</script>

<style lang="scss" scoped>
</style>
