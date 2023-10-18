<template>
    <div class="keyword-autocomplete">
        <div class="input-container">
            <svg width="16" height="16" viewBox="0 0 16 16">
                <path
                    d="M6.5 12a5.5 5.5 0 1 0 0-11 5.5 5.5 0 0 0 0 11zm4.936-1.27l4.563 4.557-.707.708-4.563-4.558a6.5 6.5 0 1 1 .707-.707z"
                    fill-rule="nonzero"
                ></path>
            </svg>
            <input
                :id="inputId"
                v-model="searchText"
                type="text"
                placeholder="Filter"
                :class="{'disabled': keywords.length === 0}"
                :disabled="keywords.length === 0"
            >
            <template v-for="(values, key) in value">
                <span v-for="(v, index) in values" :key="v" class="tag">
                    {{ v }}
                    <svg
                        focusable="false"
                        preserveAspectRatio="xMidYMid meet"
                        xmlns="http://www.w3.org/2000/svg"
                        width="16"
                        height="16"
                        viewBox="0 0 16 16"
                        aria-hidden="true"
                        style="will-change: transform;"
                        @click="removeFilter(key, index)"
                    >
                        <path
                            d="M8 1C4.1 1 1 4.1 1 8s3.1 7 7 7 7-3.1 7-7-3.1-7-7-7zm2.7 10.5L8 8.8l-2.7 2.7-.8-.8L7.2 8 4.5 5.3l.8-.8L8 7.2l2.7-2.7.8.8L8.8 8l2.7 2.7-.8.8z"
                        ></path>
                    </svg>
                </span>
            </template>
            <svg
                v-if="showRemoveAll"
                width="16"
                height="16"
                viewBox="0 0 16 16"
                xmlns="http://www.w3.org/2000/svg"
                class="clear-all"
                @click="removeAllFilters"
            >
                <path
                    d="M8 6.586L5.879 4.464 4.464 5.88 6.586 8l-2.122 2.121 1.415 1.415L8 9.414l2.121 2.122 1.415-1.415L9.414 8l2.122-2.121-1.415-1.415L8 6.586zM8 16A8 8 0 1 1 8 0a8 8 0 0 1 0 16z"
                    fill-rule="evenodd"
                ></path>
            </svg>
        </div>
        <div v-if="searchText != ''" class="suggesstions">
            <span v-for="s in suggestions" :key="s[1]" @click="addFilter(s)">{{ s[1] }}</span>
        </div>
    </div>
</template>

<script>
export default {
    name: "KeywordAutocomplete",
    props: {
        keywords: {
            type: Object,
            required: true,
            default: () => {
                return {};
            }
        },
        value: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            searchText: "",
            suggestions: [],
            showRemoveAll: false
        };
    },
    computed: {
        inputId() {
            return `keyword-autocomplete-${this._uid}`;
        }
    },
    watch: {
        searchText(query) {
            if (query == "") {
                return;
            }
            let reduced = [];
            Object.keys(this.keywords).forEach(key => {
                const res = this.keywords[key].reduce((r, keyword) => {
                    if (
                        keyword.toLowerCase().includes(query.toLowerCase()) &&
                        !this.value[key].includes(keyword)
                    ) {
                        return [...r, [key, keyword]];
                    }
                    return r;
                }, []);
                reduced = [...reduced, ...res];
            });
            this.suggestions = reduced;
        }
    },
    methods: {
        removeFilter(key, index) {
            this.value[key].splice(index, 1);
            this.$emit("update:value", this.value);
            this.showRemoveAll = this.shouldShow();
        },
        removeAllFilters() {
            const emptyValue = Object.keys(this.keywords).reduce((r, key) => {
                return { ...r, [key]: [] };
            }, {});
            this.$emit("update:value", emptyValue);
            this.showRemoveAll = false;
            this.searchText = "";
        },
        addFilter(filter) {
            const newValue = this.value;
            newValue[filter[0]].push(filter[1].toLowerCase().trim());
            this.$emit("update:value", newValue);
            this.showRemoveAll = true;
            this.searchText = "";
        },
        shouldShow() {
            Object.entries(this.value).forEach(entry => {
                if (entry.length > 0) {
                    return true;
                }
            });
            return false;
        }
    }
};
</script>

<style lang="scss" scoped>
.keyword-autocomplete {
    position: relative;
    width: 100%;
    height: 40px;

    .input-container {
        box-sizing: border-box;
        display: flex;
        flex-flow: row nowrap;
        align-items: center;
        height: 100%;
        background: #f4f7fb;

        svg {
            margin-left: 0.5rem;
            fill: #5a6872;

            &.clear-all {
                margin-right: 0.5rem;
                cursor: pointer;
            }
        }

        .tag {
            display: flex;
            flex-flow: row nowrap;
            align-items: center;
            height: 30px;
            padding: 0 12px 0 15px;
            margin-left: 0.5rem;
            color: white;
            cursor: default;
            background: #418cff;
            border-radius: 3px;

            svg {
                cursor: pointer;
                fill: white;
            }
        }

        input {
            flex-grow: 1;
            height: 100%;
            margin-left: 0.5rem;
            color: #152935;
            background: inherit;
            border: none;

            &:focus {
                outline: none;
            }

            &.disabled {
                cursor: no-drop;
                color: #5a6872;
            }
        }
    }

    .suggesstions {
        position: absolute;
        left: calc(0.5rem + 16px);
        z-index: 10;
        display: flex;
        flex-flow: column nowrap;
        box-shadow: 0 5px 7px -2px rgba(0, 0, 0, 0.1);
        background: white;
        max-height: 400px;
        overflow-y: auto;

        // min-width: 30%;
        span {
            width: 250px;
            padding: 0 15px;
            font-size: 1rem;
            line-height: 2.5rem;
            cursor: pointer;

            &:hover {
                background-color: #c9deff;
            }
        }
    }
}
</style>
