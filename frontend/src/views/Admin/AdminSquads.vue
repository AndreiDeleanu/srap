<template>
    <div class="bx--grid">
        <div class="bx--row">
            <div class="bx--col-xs-12">
                <my-data-table
                    :headers="tableHeader"
                    :rows="tableData"
                    :is-loading="isLoading"
                    :can-deactivate="true"
                    @edit-click="editSquadModalOpen"
                    @deactivate-click="deactivateSquad"
                >
                    <button
                        class="bx--btn bx--btn--primary bx--btn--sm"
                        type="button"
                        @click="openSquadModal"
                    >
                        Add Squad
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
                        heading="Add new Squad"
                        primary-button-label="Save Squad"
                        secondary-button-label="Cancel"
                        ref="squadModal"
                        @onSuccess="submitSquad"
                    >
                        <div class="bx--form-item">
                            <label for="squad_name" class="bx--label">Squad Name</label>
                            <input
                                id="squad_name"
                                type="text"
                                class="bx--text-input"
                                v-model="newSquad.name"
                            >
                        </div>
                        <div class="bx--form-item">
                            <faces-type-ahead
                                label="Focal Point"
                                :value="newSquad.focalPoint"
                                @update="(email)=>{newSquad.focalPoint = email}"
                            />
                        </div>
                        <div class="bx--form-item">
                            <faces-type-ahead
                                label="FLL"
                                :value="newSquad.fll"
                                @update="(email)=>{newSquad.fll = email}"
                            />
                        </div>
                        <div class="bx--form-item">
                            <div class="bx--select">
                                <label for="select-1234" class="bx--label">Choose Subdomain</label>
                                <select
                                    id="select-1234"
                                    class="bx--select-input"
                                    v-model="newSquad.subdomain"
                                >
                                    <option
                                        class="bx--select-option"
                                        value
                                        disabled
                                        selected
                                        hidden
                                    >Choose an option</option>
                                    <option
                                        v-for="s in subdomains"
                                        :key="s.id"
                                        class="bx--select-option"
                                        :value="s"
                                    >{{s.name}}</option>
                                </select>
                                <svg
                                    class="bx--select__arrow"
                                    width="10"
                                    height="5"
                                    viewBox="0 0 10 5"
                                >
                                    <path d="M0 0l5 4.998L10 0z" fill-rule="evenodd"></path>
                                </svg>
                            </div>
                        </div>
                    </my-modal>
                </my-data-table>
            </div>
        </div>
        <my-modal
            heading="Edit Squad"
            primary-button-label="Update Squad"
            secondary-button-label="Cancel"
            ref="squadEditModal"
            @onSuccess="updateSquadCall"
        >
            <div class="bx--form-item">
                <label for="squad_name-edit" class="bx--label">Squad Name</label>
                <input
                    id="squad_name-edit"
                    type="text"
                    class="bx--text-input"
                    v-model="updateSquad.name"
                >
            </div>
            <div class="bx--form-item">
                <faces-type-ahead
                    label="Focal Point"
                    :value="updateSquad.fp"
                    @update="(email)=>{updateSquad.fp = email}"
                />
            </div>
            <div class="bx--form-item">
                <faces-type-ahead
                    label="FLL"
                    :value="updateSquad.fll"
                    @update="(email)=>{updateSquad.fll = email}"
                />
            </div>
            <div class="bx--form-item">
                <div class="bx--select">
                    <label for="select-1235" class="bx--label">Choose Subdomain</label>
                    <select
                        id="select-1235"
                        class="bx--select-input"
                        v-model="updateSquad.subdomainId"
                    >
                        <option
                            class="bx--select-option"
                            value
                            disabled
                            selected
                            hidden
                        >Choose an option</option>
                        <option
                            v-for="s in subdomains"
                            :key="s.id"
                            class="bx--select-option"
                            :value="s.id"
                        >{{s.name}}</option>
                    </select>
                    <svg class="bx--select__arrow" width="10" height="5" viewBox="0 0 10 5">
                        <path d="M0 0l5 4.998L10 0z" fill-rule="evenodd"></path>
                    </svg>
                </div>
            </div>
        </my-modal>
    </div>
</template>

<script>
import MyDropdown from "../../components/MyDropdown";
import MyModal from "../../components/MyModal";
import MyDataTable from "../../components/MyDataTable";
import FacesTypeAhead from "../../components/FacesTypeAhead";

import { mapActions, mapState } from "vuex";
export default {
    name: "AdminSquads",
    components: {
        MyDropdown,
        MyModal,
        MyDataTable,
        FacesTypeAhead
    },
    computed: {
        ...mapState({
            squads: state => state.squads.squads,
            isLoading: state => state.squads.isLoading,
            subdomains: state => state.subdomains.subdomains
        }),
        tableData: function() {
            if (this.squads.constructor === Array) {
                return this.squads.map(squad => {
                    return {
                        data: [
                            squad.name,
                            squad.focalPoint,
                            squad.fll,
                            squad.subdomain.name,
                            squad.status
                        ],
                        metadata: {
                            id: squad.id,
                            subdomainId: squad.subdomain.id
                        }
                    };
                });
            } else {
                return [];
            }
        }
    },
    data: () => {
        return {
            newSquad: {
                name: "",
                focalPoint: "",
                fll: "",
                subdomain: {},
                domainId: -1
            },
            tableHeader: [
                { title: "Name" },
                { title: "FP Name" },
                { title: "FLL Name" },
                { title: "Subdomain" },
                { title: "Status" }
            ],
            updateSquad: {
                name: "",
                fp: "",
                fll: "",
                subdomainId: -1,
                domainId: -1
            }
        };
    },
    created: function() {
        this.getAllSquads(this.$route.params.domain);
        // this.$store.dispatch('subdomains/getAll', this.$route.params.domain);
    },
    methods: {
        ...mapActions("squads", ["getAllSquads"]),
        openSquadModal: function() {
            this.resetFields();
            const { squadModal } = this.$refs;
            squadModal.show();
        },
        submitSquad: function() {
            this.newSquad.domainId = this.$route.params.domain;
            this.$store.dispatch("squads/store", this.newSquad);
        },
        resetFields: function() {
            this.newSquad = {
                name: "",
                focalPoint: "",
                fll: ""
            };
        },
        editSquadModalOpen: function(e) {
            this.updateSquad.name = e.data[0];
            this.updateSquad.fp = e.data[1];
            this.updateSquad.fll = e.data[2];
            this.updateSquad.id = e.metadata.id;
            this.updateSquad.domainId = this.$route.params.domain;
            this.updateSquad.status = "Active";
            this.updateSquad.subdomainId = e.metadata.subdomainId;
            this.$refs.squadEditModal.show();
        },
        updateSquadCall() {
            this.updateSquad.domainId = this.$route.params.domain;
            this.$store.dispatch("squads/updateSquad", this.updateSquad);
        },
        deactivateSquad(e) {
            this.updateSquad.name = e.data[0];
            this.updateSquad.fp = e.data[1];
            this.updateSquad.fll = e.data[2];
            this.updateSquad.id = e.metadata.id;
            this.updateSquad.domainId = this.$route.params.domain;
            this.updateSquad.status = "Inactive";
            this.updateSquad.subdomainId = e.metadata.subdomainId;
            this.$store.dispatch("squads/updateSquad", this.updateSquad);
        }
    }
};
</script>

<style lang="scss" scoped>
.bx--form-item {
    margin-bottom: 0.7rem;
}
</style>
