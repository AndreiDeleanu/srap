<template>
    <div class="bx--tile">
        <div class="bx--loading-overlay" v-if="isLoading">
            <div class="bx--loading" data-loading>
                <svg class="bx--loading__svg" viewBox="-75 -75 150 150">
                    <title>Loading</title>
                    <circle cx="0" cy="0" r="37.5"></circle>
                </svg>
            </div>
        </div>
        <tabs v-else :sections="sections">
            <template :slot="'slot-' + sections[0]">
                <modal
                    :display-close-button="false"
                    :heading="'Add your Delegate'"
                    primary-button-label="Save"
                    secondary-button-label="Cancel"
                    ref="addDelegateModal"
                    @onSuccess="createDelegation"
                >
                    <div class="bx--form-item">
                        <dropdown2
                            :options="rolesOptions"
                            :value.sync="choosenRole"
                            label="Choose Role for new Delegate"
                        />
                    </div>
                    <div class="bx--form-item">
                        <faces-type-ahead
                            label="User Email"
                            :value="newDelegate"
                            @update="(email) => {newDelegate = email}"
                        />
                    </div>
                </modal>
                <modal
                    :display-close-button="false"
                    :heading="'Add your Delegate'"
                    primary-button-label="Save"
                    secondary-button-label="Cancel"
                    ref="editDelegateModal"
                    @onSuccess="updateDelegation"
                >
                    <div class="bx--form-item">
                        <dropdown2
                            :options="rolesOptions"
                            :value.sync="updatingDelegation.role"
                            label="Choose Role for new Delegate"
                        />
                    </div>
                    <div class="bx--form-item">
                        <faces-type-ahead
                            label="User Email"
                            :value="updatingDelegation.delegate"
                            @update="(email) => {updatingDelegation.delegate = email}"
                        />
                    </div>
                </modal>
                <div class="header">
                    <h4>My Delegates</h4>
                    <button
                        class="bx--btn bx--btn--primary bx--btn--sm"
                        type="button"
                        @click="openNewDelegationModal"
                    >
                        Add Delegation
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
                </div>
                <delegations-table
                    :delegations="delegates"
                    :editable="true"
                    @deleteDelegation="deleteDelegation"
                    @editDelegation="openUpdateModal"
                />
            </template>
            <template :slot="'slot-' + sections[1]">
                <div class="header">
                    <h4>My Delegators</h4>
                </div>
                <delegations-table :delegations="delegators"/>
            </template>
        </tabs>
    </div>
</template>

<script>
import Tabs from '../components/Tabs';
import Dropdown2 from '../components/Dorpdown2';
import DelegationsTable from '../components/DelegationsTable';
import Modal from '../components/MyModal';
import FacesTypeAhead from '../components/FacesTypeAhead';
import { mapState } from 'vuex';
export default {
    name: 'Delegations',
    components: {
        Tabs,
        Dropdown2,
        DelegationsTable,
        Modal,
        FacesTypeAhead
    },
    computed: {
        ...mapState({
            isLoading: state => state.delegations.isLoading,
            userRoles: state => state.user.user.roles,
            delegations: state => state.delegations.delegations,
            userEmail: state => state.user.user.user,
        }),
        rolesOptions() {
            return !!this.userRoles ? this.userRoles.map(role => {
                return {
                    label: role,
                    value: role
                }
            }) : [];
        },
        delegates() {
            const reduced = this.delegations.reduce((r, d) => {
                return d.delegator === this.userEmail ? [...r, {name: d.delegate, role: d.role, id: d.id}] : r;
            }, []);
            return reduced;
        },
        delegators() {
            const reduced = this.delegations.reduce((r, d) => {
                return d.delegate === this.userEmail ? [...r, {name: d.delegator, role: d.role}]: r;
            }, []);
            return reduced;
        }
    },
    data() {
        return {
            sections: ['Delegations', 'Delegators'],
            choosenRole: '',
            newDelegate: '',
            updatingDelegation: {
                delegator: '',
                delegate: '',
                role: '',
                id: -1,
            }
        }
    },
    watch: {
        userEmail(email) {
            this.getDelegations(email);
        }
    },
    mounted() {
        if (this.userEmail) this.getDelegations(this.userEmail);
    },
    methods: {
        getDelegations(email) {
            this.$store.dispatch('delegations/getDelegations', email);
        },
        openNewDelegationModal() {
            const {addDelegateModal} = this.$refs;
            this.choosenRole = '';
            this.newDelegate = '';
            addDelegateModal.show();
        },
        createDelegation() {
            this.$store.dispatch('delegations/addDelegation', {delegator: this.userEmail, delegate: this.newDelegate, role: this.choosenRole});
        },
        deleteDelegation(id) {
            this.$store.dispatch('delegations/deleteDelegation', {id: id, email: this.userEmail});
        },
        openUpdateModal(d) {
            this.updatingDelegation = {
                delegator: this.userEmail,
                delegate: d.name,
                role: d.role,
                id: d.id,
            };
            const {editDelegateModal} = this.$refs;
            editDelegateModal.show();
        },
        updateDelegation() {
            this.$store.dispatch('delegations/updateDelegation', this.updatingDelegation);
        }
    }
}
</script>

<style lang="scss" scoped>
    .header {
        display: flex;
        justify-content: space-between;
    }
</style>

