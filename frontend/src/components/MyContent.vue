<template>
    <div id="my-content">
        <div class="bx--type-beta" id="title">
            <span>{{domainName + ' ' + headerTitle}}</span>
            <div class="actions">
                <pdf-export class="pdf-export"/>
            </div>
        </div>
        <transition name="fade">
            <router-view id="pdf-content"></router-view>
        </transition>
    </div>
</template>

<script>
import { ROUTES } from "../router";
import { mapState } from "vuex";

import pdfExport from "./ExportToPDF.vue";

export default {
    name: "MyContent",
    components: {
        pdfExport
    },
    computed: {
        ...mapState({
            headerTitle: state => state.myContent.title,
            domainName: state => state.domains.domain.name
        }),
        authenticated: function() {
            const user = this.$store.getters["user/getUser"];
            return user.token != null;
            // return sessionStorage.getItem('token') !== undefined;
        }
    }
};
</script>

<style lang="scss" scoped>
#my-content {
    padding: 16px 20px 20px 20px;
    background: #e0e0e0;
    display: flex;
    flex: 1;
    flex-direction: column;

    #title {
        display: flex;
        justify-content: space-between;
        margin-bottom: 6px;

        span {
            font-weight: 400;
        }

        .actions {
            display: inline;
            line-height: 1rem;

            button {
                margin-left: 0.5rem;
            }
        }
    }
}

.bx--inline-notification {
    background: white;
    margin: 0 auto;
}

@media print {
    .actions {
        display: none !important;
    }
}
</style>
