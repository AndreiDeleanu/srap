<template>
    <div>
        <data-table
            :headers="dataTableHeaders"
            :rows="rows"
            :is-loading="isLoading"
            @edit-click="(row) => {editThreshold(row)}"
        >
            <button
                class="bx--btn bx--btn--primary bx--btn--sm"
                type="button"
                @click="newThreshold"
            >
                Add Threshold
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
            <modal
                :heading="modalTitle"
                primary-button-label="Save Threshold"
                secondary-button-label="Cancel"
                ref="thresholdModal"
                @onSuccess="saveThreshold"
                @onCancel="()=>{}"
                :disableSuccess="false"
            >
                <number-input
                    v-model="editingThreshold.satThreshold"
                    label="Satisfactory Threshold"
                    :limits="{from: editingThreshold.marginalThreshold + 1, to: 100}"
                />
                <number-input
                    v-model="editingThreshold.marginalThreshold"
                    label="Marginal Threshold"
                    :limits="{from: 0, to: editingThreshold.satThreshold - 1}"
                />
            </modal>
        </data-table>
    </div>
</template>

<script>
import Modal from "../../components/MyModal";
import DataTable from "../../components/MyDataTable";

import NumberInput from "../../components/NumberInput";

import { mapState } from "vuex";

export default {
    name: "AdminThresholds",
    components: {
        Modal,
        DataTable,
        NumberInput
    },
    computed: {
        ...mapState({
            isLoading: state => state.thresholdsModule.isLoading,
            thresholds: state => state.thresholdsModule.thresholds
        }),
        rows() {
            return this.thresholds.reduce((r, t) => {
                return [
                    ...r,
                    {
                        data: [
                            t.quarter,
                            t.year,
                            t.satThreshold,
                            t.marginalThreshold
                        ],
                        metadata: { id: t.id, domainId: t.domainId }
                    }
                ];
            }, []);
        }
    },
    data() {
        return {
            dataTableHeaders: [
                { title: "Quarter" },
                { title: "Year" },
                { title: "Lowest Satisfactory Score" },
                { title: "Loewst Marginal Score" }
            ],
            editingThreshold: {
                satThreshold: 90,
                marginalThreshold: 75
            },
            modalTitle: ""
        };
    },
    methods: {
        openThresholdModal() {
            const { thresholdModal } = this.$refs;
            thresholdModal.show();
        },
        newThreshold() {
            this.modalTitle = "Add a Threshold for current quarter";
            this.editingThreshold = {
                satThreshold: 90,
                marginalThreshold: 75,
                domainId: parseInt(this.$route.params.domain)
            };
            this.openThresholdModal();
        },
        editThreshold(threshold) {
            this.modalTitle = `Edit Threshold for ${threshold.quarter}Q${
                threshold.year
            }`;
            this.editingThreshold = {
                quarter: threshold.data[0],
                year: threshold.data[1],
                satThreshold: threshold.data[2],
                marginalThreshold: threshold.data[3],
                id: threshold.metadata.id,
                domainId: parseInt(this.$route.params.domain)
            };
            this.openThresholdModal();
        },
        saveThreshold() {
            if (!!this.editingThreshold.id) {
                this.$store.dispatch(
                    "thresholdsModule/updateThreshold",
                    this.editingThreshold
                );
                return;
            }
            this.$store.dispatch(
                "thresholdsModule/addThreshold",
                this.editingThreshold
            );
        }
    }
};
</script>
