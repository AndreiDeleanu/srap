<template>
    <div>
        <my-data-table
            :headers="tableHeaders"
            :rows="tableData"
            :is-loading="isLoading"
            :can-deactivate="true"
            @edit-click="openEditSubdomainModal"
            @deactivate-click="deactivateSubdomain"
        >
            <button
                class="bx--btn bx--btn--primary bx--btn--sm"
                type="button"
                @click="openSubdomainModal"
            >
                Add Subdomain
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
                heading="Add new Subdomain"
                primary-button-label="Save Subdomain"
                secondary-button-label="Cancel"
                ref="subdomainModal"
                @onSuccess="createSubdomain"
            >
                <div class="bx--form-item">
                    <label for="subdomain_name" class="bx--label">Subdomain Name</label>
                    <input
                        id="subdomain_name"
                        type="text"
                        class="bx--text-input"
                        v-model="newSubdomain.name"
                    >
                </div>
                <div class="bx--form-item">
                    <faces-type-ahead
                        label="SLL"
                        :value="newSubdomain.sllName"
                        @update="(email) => {newSubdomain.sllName = email}"
                    />
                </div>
            </my-modal>
        </my-data-table>
        <my-modal
            heading="Update Subdomain"
            primary-button-label="Save Subdomain"
            secondary-button-label="Cancel"
            ref="subdomainEditModal"
            @onSuccess="updateSubdomain"
        >
            <div class="bx--form-item">
                <label for="subdomain_name" class="bx--label">Subdomain Name</label>
                <input
                    id="subdomain_name"
                    type="text"
                    class="bx--text-input"
                    v-model="editSubdomain.name"
                >
            </div>
            <div class="bx--form-item">
                <faces-type-ahead
                    label="SLL"
                    :value="editSubdomain.sllName"
                    @update="(email) => {editSubdomain.sllName = email}"
                />
            </div>
        </my-modal>
    </div>
</template>

<script>
import MyModal from "../../components/MyModal";
import MyDataTable from "../../components/MyDataTable";
import FacesTypeAhead from "../../components/FacesTypeAhead";
import { mapState } from "vuex";

export default {
    name: "AdminSubdomains",
    components: {
        MyModal,
        MyDataTable,
        FacesTypeAhead
    },
    computed: {
        ...mapState({
            subdomains: state => state.subdomains.subdomains
        }),
        tableData() {
            if (this.subdomains.constructor === Array) {
                return this.subdomains.reduce((r, sd) => {
                    return [
                        ...r,
                        {
                            data: [sd.name, sd.sllName, sd.status],
                            metadata: { id: sd.id }
                        }
                    ];
                }, []);
            } else {
                return [];
            }
        },
        isLoading() {
            return false;
        }
    },
    data() {
        return {
            newSubdomain: {
                name: "",
                sllName: "",
                domainId: 0
            },
            editSubdomain: {
                id: 0,
                name: "",
                sllName: "",
                domainId: 0
            },
            tableHeaders: [
                { title: "Name" },
                { title: "SLL" },
                { title: "Status" }
            ]
        };
    },
    methods: {
        openSubdomainModal: function() {
            this.newSubdomain = {
                name: "",
                sllName: "",
                domainId: parseInt(this.$route.params.domain)
            };
            this.resetFields();
            const { subdomainModal } = this.$refs;
            subdomainModal.show();
        },
        resetFields: function() {
            this.newSubdomain = {
                domainId: this.$route.params.domain,
                name: "",
                sllName: ""
            };
        },
        createSubdomain() {
            this.newSubdomain.domainId = this.$route.params.domain;
            this.$store.dispatch(
                "subdomains/createSubdomain",
                this.newSubdomain
            );
        },
        openEditSubdomainModal(data) {
            this.editSubdomain = {
                name: data.data[0],
                sllName: data.data[1],
                id: data.metadata.id,
                domainId: this.$route.params.domain,
                status: "Active"
            };
            const { subdomainEditModal } = this.$refs;
            subdomainEditModal.show();
        },
        updateSubdomain() {
            this.$store.dispatch("subdomains/updateDomain", this.editSubdomain);
        },
        deactivateSubdomain(data) {
            this.editSubdomain = {
                name: data.data[0],
                sllName: data.data[1],
                id: data.metadata.id,
                domainId: this.$route.params.domain,
                status: "Inactive"
            };
            this.$store.dispatch("subdomains/updateDomain", this.editSubdomain);
        }
    }
};
</script>

<style lang="scss" scoped>
.bx--form-item {
    margin-bottom: 0.7rem;
}
</style>
