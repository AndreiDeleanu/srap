<template>
    <div v-if="processes !== undefined" class="processesContainer">
        <!-- :display-close-button="false" -->
        <modal
            :heading="'Edit/View ' + updatingProcess.processName + ' Process'"
            primary-button-label="Update Process"
            secondary-button-label="Cancel"
            @onSuccess="updateProcess"
            @onCancel="cancelEdit"
            ref="editProcessModal"
        >
            <div class="hint">
                <label class="bx--label">Description</label>
                <tooltyp :text="updatingProcess.description">
                    <svg width="16" height="16" viewBox="0 0 16 16" fill="#3d70b2">
                        <path
                            d="M8 14.5a6.5 6.5 0 1 0 0-13 6.5 6.5 0 0 0 0 13zM8 16A8 8 0 1 1 8 0a8 8 0 0 1 0 16z"
                        ></path>
                        <path d="M9 13H7V7h2z"></path>
                        <path d="M7 4a1 1 0 1 1 2 0 1 1 0 1 1-2 0"></path>
                    </svg>
                    {{updatingProcess.description}}
                </tooltyp>
            </div>
            <dropdown2
                default-text="Pick Rating"
                :options="ratings"
                :value.sync="updatingProcess.rating"
                label="Choose Rating for Process"
            />
            <div class="bx--form-item comment">
                <label
                    class="bx--label"
                    :class="{'bx--label--disabled': comentable}"
                >Comment for Process Rating</label>
                <jodit
                    :value.sync="updatingProcess.comment"
                    id="processes-editor"
                    @input="val => { updatingProcess.comment = val }"
                />
            </div>
        </modal>
        <div class="bx--row processes" v-for="(row, i) in processes" :key="i + 'row'">
            <div class="bx--col-xs-3" v-for="(p, j) in row" :key="p.id">
                <process-tile
                    :indexes="[i,j]"
                    :title="p.processName"
                    :rating="p.rating"
                    :comment="p.comment"
                    :id="p.processId"
                    :description="p.description"
                    @edit="editProcess"
                />
            </div>
        </div>
    </div>
</template>

<script>
import Jodit from '../../components/Jodit';
import Modal from '../../components/MyModal';
import Dropdown2 from '../../components/Dorpdown2';
import ProcessTile from './ProcessTile';
import Tooltyp from '../../components/Tooltip';
export default {
    name: 'Processes',
    components: {
        Jodit,
        Modal,
        Dropdown2,
        ProcessTile,
        Tooltyp
    },
    props: {
        processes: {
            type: Array,
            default: () => []
        },
        comentable: {
            type: Boolean,
            default: true
        }
    },
    data() {
        return {
            rowID: 0,
            propID: 0,
            newValue: '',
            updatingProcess: {
                indexes: [],
                processName: '',
                rating: '',
                comment: '',
                processId: -1,
                description: ''
            },
            ratings: [
                {label:"N/A", value: 'n/a'},
                {label:"Satisfactory", value: 'Sat'},
                {label:"Marginal", value: 'Marginal'},
                {label:"Unsatisfactory", value:'Unsat'}
            ],
            processModalHeading: ''
        }
    },
    methods: {
        updateProcess() {
            this.processes[this.updatingProcess.indexes[0]][this.updatingProcess.indexes[1]].rating = this.updatingProcess.rating;
            this.processes[this.updatingProcess.indexes[0]][this.updatingProcess.indexes[1]].comment = this.updatingProcess.comment;
            this.$emit('processesUpdated');
            //  = this.updatingProcess;
        },
        cancelEdit() {
            this.updatingProcess = {
                indexes: [],
                processName: '',
                rating: '',
                comment: '',
                processId: -1,
                description: ''
            };
        },
        editProcess(process) {
            this.updatingProcess = {...process};
            const {editProcessModal} = this.$refs;
            editProcessModal.show();
        }
    }
}
</script>

<style lang="scss" scoped>
    .processesContainer {
        margin-top: 1.25rem;
    }
    .processes {
        &:not(:last-of-type) {
            margin-bottom: 20px;
        }
    }
</style>

