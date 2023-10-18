<template>
    <div>
        <div
            v-if="delegations.length === 0"
            data-notification
            class="bx--inline-notification bx--inline-notification--info"
            role="alert"
        >
            <div class="bx--inline-notification__details">
                <svg
                    class="bx--inline-notification__icon"
                    width="16"
                    height="16"
                    viewBox="0 0 16 16"
                    xmlns="http://www.w3.org/2000/svg"
                >
                    <path
                        d="M8 16A8 8 0 1 1 8 0a8 8 0 0 1 0 16zm1-3V7H7v6h2zM8 5a1 1 0 1 0 0-2 1 1 0 0 0 0 2z"
                    ></path>
                </svg>
                <div class="bx--inline-notification__text-wrapper">
                    <p class="bx--inline-notification__title">No Delegations yet.</p>
                    <!-- <p class="bx--inline-notification__subtitle">Subtitle text goes here.</p> -->
                </div>
            </div>
        </div>
        <section v-else class="bx--structured-list">
            <div class="bx--structured-list-thead">
                <div class="bx--structured-list-row bx--structured-list-row--header-row">
                    <div class="bx--structured-list-th">Email</div>
                    <div class="bx--structured-list-th">Role</div>
                    <div class="bx--structured-list-th" v-if="editable">Actions</div>
                </div>
            </div>
            <div class="bx--structured-list-tbody">
                <div
                    class="bx--structured-list-row"
                    v-for="(delegation, i) in delegations"
                    :key="'delegation-'+i"
                >
                    <div
                        class="bx--structured-list-td bx--structured-list-content--nowrap"
                    >{{delegation.name}}</div>
                    <div class="bx--structured-list-td">{{delegation.role}}</div>
                    <div class="bx--structured-list-td" v-if="editable">
                        <button
                            class="bx--btn bx--btn--ghost bx--btn--sm"
                            type="button"
                            @click="editDelegation(delegation)"
                        >
                            Edit
                            <svg width="16" height="16" viewBox="0 0 16 16">
                                <path
                                    d="M7.926 3.38L1.002 9.72V12h2.304l6.926-6.316L7.926 3.38zm.738-.675l2.308 2.304 1.451-1.324-2.308-2.309-1.451 1.329zM.002 9.28L9.439.639a1 1 0 0 1 1.383.03l2.309 2.309a1 1 0 0 1-.034 1.446L3.694 13H.002V9.28zM0 16.013v-1h16v1z"
                                ></path>
                            </svg>
                        </button>
                        <button
                            class="bx--btn bx--btn--ghost bx--btn--sm"
                            type="button"
                            @click="deleteDelegation(delegation.id)"
                        >
                            Delete
                            <svg width="12" height="16" viewBox="0 0 12 16">
                                <path
                                    d="M11 4v11c0 .6-.4 1-1 1H2c-.6 0-1-.4-1-1V4H0V3h12v1h-1zM2 4v11h8V4H2z"
                                ></path>
                                <path d="M4 6h1v7H4zm3 0h1v7H7zM3 1V0h6v1z"></path>
                            </svg>
                        </button>
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>

<script>
export default {
    name: 'DelegationsTable',
    props: {
        delegations: {
            type: Array,
            default: () => []
        },
        editable: {
            type: Boolean,
            default: false
        }
    },
    methods: {
        editDelegation(delegation) {
            this.$emit('editDelegation', delegation);
        },
        deleteDelegation(id) {
            this.$emit('deleteDelegation', id);
        }
    }
}
</script>

<style lang="scss" scoped>
    .bx--btn--ghost {
        path {
            fill: currentColor;
        }
    }
</style>
