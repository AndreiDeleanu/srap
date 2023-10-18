<template>
    <div class="bx--grid">
        <div class="bx--row">
            <div class="bx--col-xs-12">
                <my-data-table
                    :headers="tableHeaders"
                    :rows="tableData"
                    :is-loading="isLoading"
                    @edit-click="editDomain"
                >
                    <button
                        class="bx--btn bx--btn--primary bx--btn--sm"
                        type="button"
                        @click="openDomainModal"
                    >
                        Add Domain
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
                        heading="Add new Domain"
                        primary-button-label="Save Domain"
                        secondary-button-label="Cancel"
                        ref="domainModal"
                        @onSuccess="submitDomain"
                    >
                        <div class="bx--form-item">
                            <label for="domain_name" class="bx--label">Domain Name</label>
                            <input
                                id="domain_name"
                                type="text"
                                class="bx--text-input"
                                v-model="newDomain.name"
                            >
                        </div>
                        <div class="bx--form-item">
                            <faces-type-ahead
                                label="Domain Owner"
                                :value="newDomain.owner"
                                @update="(email)=>{newDomain.owner = email}"
                            />
                        </div>
                        <div class="bx--form-item">
                            <faces-type-ahead
                                label="Domain Admin"
                                :value="newDomain.admin"
                                @update="(email)=>{newDomain.admin = email}"
                            />
                        </div>
                        <div class="bx--form-item">
                            <label for="help" class="bx--label">Domain Help URL</label>
                            <input
                                id="help"
                                type="text"
                                class="bx--text-input"
                                v-model="newDomain.help"
                                autocomplete="off"
                            >
                        </div>
                    </my-modal>
                </my-data-table>

                <my-modal
                    heading="Edit Domain"
                    primary-button-label="Save Domain"
                    secondary-button-label="Cancel"
                    ref="domainEditModal"
                    @onSuccess="saveDomain"
                >
                    <div class="bx--form-item">
                        <label for="domain_name" class="bx--label">Domain Name</label>
                        <input
                            id="domain_name"
                            type="text"
                            class="bx--text-input"
                            v-model="editing.name"
                        >
                    </div>
                    <div class="bx--form-item">
                        <faces-type-ahead
                            label="Domain Owner"
                            :value="editing.owner"
                            @update="(email)=>{editing.owner = email}"
                        />
                    </div>
                    <div class="bx--form-item">
                        <faces-type-ahead
                            label="Domain Admin"
                            :value="editing.admin"
                            @update="(email)=>{editing.admin = email}"
                        />
                    </div>
                    <div class="bx--form-item">
                        <label for="edit_help" class="bx--label">Domain Help URL</label>
                        <input
                            id="edit_help"
                            type="url"
                            class="bx--text-input"
                            v-model="editing.help"
                            autocomplete="off"
                        >
                    </div>
                </my-modal>
            </div>
        </div>
    </div>
</template>

<script>
import MyDataTable from "../../components/MyDataTable";
import MyModal from "../../components/MyModal";
import FacesTypeAhead from "../../components/FacesTypeAhead";
import { mapState } from "vuex";

export default {
    name: "Domains",
    components: {
        MyDataTable,
        MyModal,
        FacesTypeAhead
    },
    computed: {
        ...mapState({
            isLoading: state => state.domains.isLoading,
            domains: state => state.domains.domains
        }),
        tableData() {
            if (this.domains.constructor === Array) {
                return this.domains.reduce((r, d) => {
                    return [
                        ...r,
                        {
                            data: [d.name, d.owner, d.admin, d.help],
                            metadata: { id: d.id }
                        }
                    ];
                }, []);
            } else {
                return [];
            }
        }
    },
    data: () => {
        return {
            tableHeaders: [
                { title: "Name" },
                { title: "Owner" },
                { title: "Admin" },
                { title: "Help URL" }
            ],
            newDomain: {
                name: "",
                owner: "",
                admin: "",
                help: "https://",
                status: "Active"
            },
            editing: {
                id: null,
                name: "",
                owner: "",
                admin: "",
                help: "",
                status: "Active"
            }
        };
    },
    created() {
        this.$store.dispatch("domains/getAll");
    },
    methods: {
        editDomain: function(data) {
            this.editing.id = data.metadata.id;
            this.editing.name = data.data[0];
            this.editing.owner = data.data[1];
            this.editing.admin = data.data[2];
            this.editing.help = data.data[3];
            this.editing.status = "Active";
            const { domainEditModal } = this.$refs;
            domainEditModal.show();
        },
        openDomainModal: function() {
            this.newDomain = {
                name: "",
                owner: "",
                admin: "",
                help: "https://",
                status: "Active"
            };
            const { domainModal } = this.$refs;
            domainModal.show();
        },
        submitDomain: function() {
            this.$store.dispatch("domains/createDomain", this.newDomain);
        },
        saveDomain: function() {
            this.$store.dispatch("domains/updateDomain", this.editing);
        }
    }
};
</script>
