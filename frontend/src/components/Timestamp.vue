<template>
    <div class="bx--tile header">
        <div>
            Created by:
            <strong>{{history.createdBy}}</strong> @
            <strong>{{createdTime}}</strong>
        </div>
        <div>
            Last modified by:
            <strong>{{history.lastModifiedBy}}</strong> @
            <strong>{{lastModifiedTime}}</strong>
        </div>
    </div>
</template>

<script>
import moment from 'moment';
export default {
    name: 'Timestamp',
    props: {
        history: {
            type: Object,
            default: () => {
                return {
                    createdBy:"SRAP_SYSTEM",
                    createdTime:1548429788206,
                    lastModifiedBy:"SRAP_SYSTEM",
                    lastModifiedTime:1548429788206,
                }
            }
        },
    },
    computed: {
        createdTime() {
            return moment(this.history.createdTime).format('DD/MM/YYYY');
        },
        lastModifiedTime() {
            return moment(this.history.lastModifiedTime).format('DD/MM/YYYY');
        }
    }
}
</script>

<style lang="scss" scoped>
    .bx--tile {
        margin-top: 1rem;
        display: flex;
        justify-content: space-around;
        box-shadow: unset;
        border: unset;
        background: #fbfbfb;
    }
</style>

