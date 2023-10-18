<template>
    <div class="bx--tile header top">
        <my-modal
            :display-close-button="false"
            :heading="'Domain Owner Coment'"
            :primary-button-label="'Update'"
            :secondary-button-label="'Cancel'"
            :small-heading="''"
            ref="doComentModal"
            @onSuccess="updateComment"
        >
            <jodit
                :value.sync="comment"
                @input="(val) => {
                    clicky(val)
                }"
            ></jodit>
        </my-modal>
        <dropdown2
            :default-text="'Choose rating'"
            :label="ddLabel"
            :options="doAssessmentOptions"
            :value.sync="newRating"
            :disabled="disableInputs"
        ></dropdown2>
        <dropdown2
            class="projection"
            v-if="showProjection"
            :default-text="'Choose Projection rating'"
            label="Next quarter Projection"
            :options="doAssessmentOptions"
            :value.sync="newProjection"
            :disabled="disableInputs"
        />
        <div class="doComment">
            <div class="comment">
                <label class="bx--label">{{cLabel}}</label>
                <div v-html="comment"></div>
            </div>
            <button
                class="bx--btn bx--btn--ghost bx--btn--sm"
                type="button"
                @click="openDoCommentModal"
                :disabled="disableInputs"
            >
                Edit Comment
                <svg class="bx--btn__icon" height="16" viewBox="0 0 16 16" width="16">
                    <path
                        d="M7.926 3.38L1.002 9.72V12h2.304l6.926-6.316L7.926 3.38zm.738-.675l2.308 2.304 1.451-1.324-2.308-2.309-1.451 1.329zM.002 9.28L9.439.639a1 1 0 0 1 1.383.03l2.309 2.309a1 1 0 0 1-.034 1.446L3.694 13H.002V9.28zM0 16.013v-1h16v1z"
                    ></path>
                </svg>
            </button>
        </div>
        <div class="buttons">
            <button
                class="bx--btn bx--btn--secondary bx--btn--sm"
                @click="$emit('calculate')"
            >Calculate from collected Data</button>
            <button class="bx--btn bx--btn--primary bx--btn--sm" @click="$emit('save')">
                Save
                <svg class="bx--btn__icon" height="16" viewBox="0 0 16 16" width="16">
                    <path
                        d="M12 1.597v2.406a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V1H1v14h3V9a1 1 0 0 1 1-1h6a1 1 0 0 1 1 1v6h3V4.702l-3-3.105zM1 0h10.848L16 4.298V15a1 1 0 0 1-1 1H1a1 1 0 0 1-1-1V1a1 1 0 0 1 1-1zm10 9H5v5.992l6 .007V9zM5 1v3.003h6V1H5z"
                    ></path>
                </svg>
            </button>
        </div>
    </div>
</template>

<script>
import MyModal from "../../components/MyModal";
import MyDropdown from "../../components/MyDropdown";
import Dropdown2 from "../../components/Dorpdown2";
import Jodit from "../../components/Jodit";
export default {
    name: "doInputs",
    components: {
        MyModal,
        MyDropdown,
        Dropdown2,
        Jodit
    },
    props: {
        rating: {
            type: String,
            required: true,
            default: ""
        },
        comment: {
            type: String,
            required: true,
            default: ""
        },
        disableInputs: {
            type: Boolean,
            default: false
        },
        ddLabel: {
            type: String,
            default: "Domain Owner Assessment Override"
        },
        cLabel: {
            type: String,
            default: "Domain Owner Comment"
        },
        projection: {
            type: String,
            default: ""
        },
        showProjection: {
            type: Boolean,
            default: false
        }
    },
    data() {
        return {
            doAssessmentOptions: [
                { label: "Satisfactory", value: "Sat" },
                { label: "Marginal", value: "Marginal" },
                { label: "Unsatisfactory", value: "Unsat" }
            ],
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
            newComment: "",
            newRating: "",
            newProjection: ""
        };
    },
    mounted() {
        this.newRating = this.rating;
        this.newProjection = this.projection;
    },
    watch: {
        newRating(val) {
            this.$emit("update:rating", val);
        },
        newProjection(val) {
            this.$emit("update:projection", val);
        }
    },
    methods: {
        openDoCommentModal() {
            this.$refs.doComentModal.show();
        },
        clicky(value) {
            this.newComment = value;
        },
        updateComment() {
            this.$emit("update:comment", this.newComment);
        }
    }
};
</script>

<style lang="scss" scoped>
.header {
    display: flex;
    flex-flow: row nowrap;

    &.top {
        align-items: flex-start !important;
    }

    .projection {
        margin-left: 1rem;
    }

    .doComment {
        display: flex;
        flex-flow: row nowrap;
        margin-left: 1rem;
        align-items: flex-end;
        flex-grow: 2;

        .comment {
            div {
                min-height: 40px;
                width: 30rem;
                background-color: #f4f7fb;
                padding: 13px 16px;
                // overflow: hidden;
                // white-space: nowrap;
                // text-overflow: ellipsis;
                p {
                    line-height: 40px;
                    padding: 0;
                }
            }
        }
        button {
            margin-left: 0.5rem;
        }
    }

    .buttons {
        margin-left: auto;
    }
}
</style>

