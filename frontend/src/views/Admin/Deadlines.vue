<template>
    <data-table :headers="headers" :rows="rows" :is-loading="isLoading" @edit-click="edit">
        <button class="bx--btn bx--btn--primary bx--btn--sm" type="button" @click="openSquadModal">
            Add Deadline
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
            primary-button-label="Save Deadline"
            secondary-button-label="Cancel"
            ref="deadlineModal"
            @onSuccess="saveDeadline"
            @onCancel="clearDeadline"
            :disableSuccess="deadline.quarter === '' || deadline.fpSubmitDeadline === '' || deadline.fllSignoffDeadline === '' || deadline.sllRollupDeadline === ''"
        >
            <drop-down
                :options="quarters"
                :value.sync="deadline.quarter"
                :disabled="lockQaurterSelect"
                label="Choose quarter for deadlines"
                defaultText="Choose Quarter"
            />
            <date-picker
                label="FP Submition Deadline"
                name="submitionDeadline"
                :val.sync="deadline.fpSubmitDeadline"
                :minDate="firstDay"
                :maxDate="lastDay"
                :required="true"
                :disabled="deadline.quarter === ''"
            />
            <date-picker
                label="FLL Sign-off Deadline"
                name="signoffDeadline"
                :val.sync="deadline.fllSignoffDeadline"
                :required="true"
                :minDate="deadline.fpSubmitDeadline"
                :maxDate="lastDay"
                :disabled="deadline.fpSubmitDeadline === ''"
            />
            <date-picker
                label="Subdomain roll-up Deadline"
                name="rollupDeadline"
                :val.sync="deadline.sllRollupDeadline"
                :required="true"
                :minDate="deadline.fllSignoffDeadline"
                :maxDate="lastDay"
                :disabled="deadline.fllSignoffDeadline === ''"
            />
        </modal>
    </data-table>
</template>

<script>
import DataTable from '../../components/MyDataTable';
import Modal from '../../components/MyModal';
import moment,{ months } from 'moment';
import DatePicker from '../../components/DatePicker';
import DropDown from '../../components/Dorpdown2';
import { mapState } from 'vuex';
import helpers from '../../helpers/helpers';
export default {
    name: 'Deadlines',
    components: {
        DataTable,
        Modal,
        DatePicker,
        DropDown
    },
    computed: {
        ...mapState({
            isLoading: state => state.deadlines.isLoading,
            deadlines: state => state.deadlines.deadlines
        }),
        rows() {
            return this.deadlines.map(d => {
                return {
                    data: [
                        d.quarter,
                        moment(d.fpSubmitDeadline).format('DD/MM/YYYY'),
                        moment(d.fllSignoffDeadline).format('DD/MM/YYYY'),
                        moment(d.sllRollupDeadline).format('DD/MM/YYYY')
                    ],
                    meta: {
                        id: d.id,
                        domainId: d.domainId
                    }
                }
            });
        },
        thisQuarter() {
            return helpers.getCurrentQYYYY();
        },
        quarters() {
            return helpers.get3nextQuarters().map(q => {
                const qq = this.deadlines.find(d => d.quarter == q)
                return {
                    value: q,
                    label: q,
                    disabled: !!qq
                }
            });
        },
        firstDay() {
            if(this.quarter == null || this.quarter == '') { return '01/01/2019' };
            if(this.thisQuarter === this.quarter) {
                return moment().format('DD/MM/YYYY');
            }
            const quarterData = this.quarter.split('Q');
            const date = moment('01/01/'+quarterData[1]);
            date.add(quarterData[0]-1, 'quarters');
            return date.startOf('quarter').format('DD/MM/YYYY');
        },
        lastDay() {
            if(this.quarter == null || this.quarter == '') { return '01/01/2019' };
            if (this.thisQuarter == this.quarter) {
                return moment().endOf('quarter').format('DD/MM/YYYY');
            }
            const quarterData = this.quarter.split('Q');
            const date = moment('01/01/'+quarterData[1]);
            date.add(3*(quarterData[0]-1), 'months');
            return date.endOf('quarter').format('DD/MM/YYYY');

        },
        quarter() {
            return this.deadline.quarter
        }
    },
    data() {
        return {
            headers: [
                {title: 'Quarter'},
                {title: 'FP Submition Deadline'},
                {title: 'FLL Sign-off Deadline'},
                {title: 'Subdomain roll-up Deadline'},
            ],
            deadline: {
                quarter: null,
                fpSubmitDeadline: '',
                fllSignoffDeadline: '',
                sllRollupDeadline: ''
            },
            modalTitle: '',
            lockQaurterSelect: false,
        }
    },
    watch: {
        quarter(q, oldQ) {
            if (oldQ == null || oldQ == '')
                return;
            this.deadline.fpSubmitDeadline = '';
            this.deadline.fllSignoffDeadline = '';
            this.deadline.fllSignoffDeadline = '';
        }
    },
    methods: {
        edit(e) {
            this.lockQaurterSelect = true;
            this.modalTitle = 'Edit Deadline for ' + e.data[0];
            this.deadline.id = e.meta.id;
            this.deadline.domainId = e.meta.domainId;
            this.deadline.quarter = e.data[0];
            this.deadline.fpSubmitDeadline = e.data[1];
            this.deadline.fllSignoffDeadline = e.data[2];
            this.deadline.sllRollupDeadline = e.data[3];
            const {deadlineModal} = this.$refs;
            deadlineModal.show();
        },
        openSquadModal() {
            this.lockQaurterSelect = false;
            this.modalTitle = 'Add new Deadline';
            this.deadline = {
                domainId: this.$route.params.domain,
                quarter: '',
                fpSubmitDeadline: '',
                fllSignoffDeadline: '',
                sllRollupDeadline: ''
            };
            const {deadlineModal} = this.$refs;
            deadlineModal.show();
        },
        saveDeadline() {
            const newD = {
                domainId: this.deadline.domainId,
                quarter: this.deadline.quarter,
                fpSubmitDeadline: moment(this.deadline.fpSubmitDeadline, 'DD/MM/YYYY').valueOf(),
                fllSignoffDeadline: moment(this.deadline.fllSignoffDeadline, 'DD/MM/YYYY').valueOf(),
                sllRollupDeadline: moment(this.deadline.sllRollupDeadline, 'DD/MM/YYYY').valueOf(),
            }
            if (!!this.deadline.id) {
                newD.id = this.deadline.id;
                this.$store.dispatch('deadlines/updateDeadline', newD);
            } else {
                this.$store.dispatch('deadlines/createDeadline', newD);
            }
        },
        clearDeadline() {
            this.deadline = {
                quarter: null,
                fpSubmitDeadline: '',
                fllSignoffDeadline: '',
                sllRollupDeadline: ''
            };
        }
    },
    created() {
        const {domain} = this.$route.params;
        this.$store.dispatch('deadlines/getDomainDeadlines', domain);
    }
}
</script>
