<template>
    <div v-if="processes !== undefined">
        <my-modal
            :display-close-button="false"
            :heading="'Comment for: ' + processes[rowID][propID].name"
            :primary-button-label="'Update'"
            :secondary-button-label="'Cancel'"
            @onSuccess="updateComment"
            ref="DACommentModal"
            v-if="comentable"
        >
            <jodit
                :value.sync="processes[rowID][propID].daComment"
                :buttons="joditButtons"
                :config="joditConfig"
                :id="'daCommentEditor'"
                @input="(val) => {
                    joditInput(val);
                }"
            ></jodit>
        </my-modal>
        <div class="bx--row processes" v-for="(row, index) in processes" :key="index + 'row'">
            <div class="bx--col-xs-3" v-for="(p, i) in row" :key="p.id">
                <div class="bx--tile process">
                    <div class="bx--type--delta process-name">{{p.name ? p.name : p.processName}}</div>
                    <div class="info">
                        <div v-html="p.daComment" v-if="comentable"></div>
                        <p v-if="comentable">
                            <button
                                class="bx--btn bx--btn--ghost bx--btn--sm"
                                @click="setIndexes(index, i)"
                            >Edit</button>
                        </p>
                        <div
                            :class="'bx--tag bx--tag--' + (p.average === 'Sat' ? 'community' : p.average === 'Marginal' ? 'private' : p.average === 'N/A' ? 'beta' : 'custom')"
                        >{{p.average ? p.average : 'No data yet'}}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import MyModal from "../../components/MyModal";
import Jodit from "../../components/Jodit";
export default {
    name: "Processes",
    components: {
        MyModal,
        Jodit
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
            joditButtons: [
                "paragraph",
                "|",
                "bold",
                "italic",
                "|",
                "ul",
                "link"
            ],
            joditConfig: {
                autofocus: true,
                allowResizeY: false,
                toolbarSticky: false,
                showWordsCounter: false,
                showXPathInStatusbar: false,
                height: 250,
                limitChars: 750
            },
            newValue: ""
        };
    },
    methods: {
        setIndexes(row, col) {
            this.rowID = row;
            this.propID = col;
            this.openDACommentModal();
        },
        openDACommentModal() {
            this.$refs.DACommentModal.show();
        },
        joditInput(val) {
            this.newValue = val;
        },
        updateComment() {
            this.processes[this.rowID][this.propID].daComment = this.newValue;
            this.newValue = "";
        }
    }
};
</script>

<style lang="scss" scoped>
.processes {
    &:not(:last-of-type) {
        margin-bottom: 20px;
    }

    .bx--tile {
        min-height: 11rem;
        height: 100%;
        border-left: 6px;
        border-style: solid;
        display: flex;
        flex-direction: column;
        padding: 9px;

        .bx--type--delta {
            font-size: 1.125rem;
            line-height: 1.25;
            font-weight: 600;
        }

        &.incomplete {
            border-left-color: #e0182d;
        }

        &.correct {
            border-left-color: #5aa700;
        }

        &.untouched {
            border-left-color: #cdd1d4;
        }

        .process-name {
            text-align: center;
        }

        .info {
            display: flex;
            flex-direction: column;
            margin: auto 0 0 0;

            [class*="data-"] {
                align-items: center;
                margin: 0 0 0.5rem auto;
                display: flex;

                span {
                    font-style: italic;
                    font-weight: 300 !important;
                    text-align: right;
                }

                svg {
                    height: 1rem;
                    width: 25px;
                }
            }

            .data- {
                &untouched {
                    color: #a0a0a0;
                }

                &incomplete {
                    color: #e0182d;
                    fill: #e0182d;
                }

                &correct {
                    color: #5aa700;
                    fill: #5aa700;
                }
            }

            .edit {
                display: flex;
                margin-left: auto;
            }
        }
        .bx--tag {
            margin: 0 0 0 auto;
            color: #152935 !important;

            &.bx--tag--custom {
                background-color: #f5a0a9;
            }
        }
    }
}
</style>

