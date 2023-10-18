<template>
    <div class="bx--tile process" :class="validationClass">
        <div class="bx--type--delta process-name">{{title}}</div>
        <div class="info">
            <!-- <div v-html="comment"></div> -->
            <div :class="'data-'+validationClass">{{validationText}}</div>
            <div v-if="rating" :class="'bx--tag bx--tag--' + tagClass">{{ratingValue}}</div>
            <div>
                <button type="button" class="bx--btn bx--btn--ghost bx--btn--sm edit" @click="edit">
                    Edit
                    <svg height="16" viewBox="0 0 16 16" width="16" class="bx--btn__icon">
                        <path
                            d="M7.926 3.38L1.002 9.72V12h2.304l6.926-6.316L7.926 3.38zm.738-.675l2.308 2.304 1.451-1.324-2.308-2.309-1.451 1.329zM.002 9.28L9.439.639a1 1 0 0 1 1.383.03l2.309 2.309a1 1 0 0 1-.034 1.446L3.694 13H.002V9.28zM0 16.013v-1h16v1z"
                        ></path>
                    </svg>
                </button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "ProcessTile",
    props: {
        title: {
            type: String,
            default: "Process"
        },
        rating: {
            type: String,
            default: ""
        },
        comment: {
            type: String,
            default: ""
        },
        disabled: {
            type: Boolean,
            default: false
        },
        id: {
            type: Number,
            required: true
        },
        indexes: {
            type: Array
        },
        description: {
            type: String,
            required: true
        }
    },
    computed: {
        tagClass() {
            return this.rating === "Sat"
                ? "community"
                : this.rating === "Marginal"
                ? "private"
                : this.rating.toLowerCase() === "n/a"
                ? "beta"
                : "custom";
        },
        ratingValue() {
            return this.rating
                ? this.rating === "Sat"
                    ? "Satisfactory"
                    : this.rating === "Unsat"
                    ? "Unsatisfactory"
                    : this.rating.toLowerCase() === "n/a"
                    ? "N/A"
                    : "Marginal"
                : "";
        },
        validationClass() {
            return (this.rating == "" || this.rating == null) &&
                this.comment == ""
                ? "untouched"
                : this.comment != "" &&
                  (this.rating == "" || this.rating == null)
                ? "rating"
                : this.rating !== "Sat" && this.comment == ""
                ? "comment"
                : "ok";
        },
        validationText() {
            let text;
            switch (this.validationClass) {
                case "ok":
                    text = "All fields are filled in";
                    break;
                case "untouched":
                    text = "Please fill in the data";
                    break;
                case "rating":
                    text = "Rating required";
                    break;
                default:
                    text = "Comment required";
                    break;
            }
            return text;
        }
    },
    methods: {
        edit() {
            this.$emit("edit", {
                indexes: this.indexes,
                processName: this.title,
                rating: this.rating,
                comment: this.comment,
                processId: this.id,
                description: this.description
            });
        }
    }
};
</script>

<style lang="scss" scoped>
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

    &.rating,
    &.comment {
        border-left-color: #e0182d;
    }

    &.ok {
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
            font-style: italic;
            font-weight: 300 !important;
            text-align: right;

            svg {
                height: 1rem;
                width: 25px;
            }
        }

        .data- {
            &untouched {
                color: #a0a0a0;
            }

            &rating,
            &comment {
                color: #e0182d;
                fill: #e0182d;
            }

            &ok {
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
</style>

