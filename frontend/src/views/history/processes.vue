<template>
    <div v-if="processes !== undefined">
        <div class="bx--row processes" v-for="(row, index) in processes" :key="index + 'row'">
            <div class="bx--col-xs-3" v-for="(p) in row" :key="p.id">
                <div class="bx--tile process">
                    <div class="bx--type--delta process-name">{{p.name ? p.name : p.processName}}</div>
                    <div class="info">
                        <div v-html="p.daComment"></div>
                        <div
                            :class="'bx--tag bx--tag--' + (p.average === 'Sat' ? 'community' : p.average === 'Marginal' ? 'private' : p.average === 'n/a' ? 'beta' : 'custom')"
                        >{{p.average ? (p.average === 'Sat' ? 'Satisfactory' : p.average === 'Unsat' ? 'Unsatisfactory' : p.average ==='n/a' ? 'N/A' : 'Marginal') : 'No data yet'}}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "Processes",
    components: {},
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
            newValue: ""
        };
    },
    methods: {}
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

