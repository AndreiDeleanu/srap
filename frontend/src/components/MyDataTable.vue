<template>
    <div>
        <div :id="dtUuid" class="bx--data-table-v2-container" data-table-v2>
            <section class="bx--table-toolbar">
                <div class="bx--toolbar-search-container" v-if="!disableSearch">
                    <div
                        :id="searchUuid"
                        data-search
                        class="bx--search bx--search--sm"
                        role="search"
                    >
                        <svg
                            class="bx--search-magnifier"
                            width="16"
                            height="16"
                            viewBox="0 0 16 16"
                            :class="{'disabled' : isLoading}"
                        >
                            <path
                                d="M6.5 12a5.5 5.5 0 1 0 0-11 5.5 5.5 0 0 0 0 11zm4.936-1.27l4.563 4.557-.707.708-4.563-4.558a6.5 6.5 0 1 1 .707-.707z"
                                fill-rule="nonzero"
                            ></path>
                        </svg>
                        <label
                            :id="'search-input-label-' + _uid"
                            class="bx--label"
                            for="search__input-2"
                        >Search</label>
                        <input
                            v-model="searchText"
                            :disabled="isLoading"
                            ref="searchInput"
                            class="bx--search-input"
                            type="text"
                            id="search__input-2"
                            role="search"
                            placeholder="Search"
                            :aria-labelledby="'search-input-label-' + _uid"
                        >
                        <button
                            class="bx--search-close bx--search-close--hidden"
                            title="Clear search input"
                            aria-label="Clear search input"
                            @click="onSearchClear()"
                        >
                            <svg
                                width="16"
                                height="16"
                                viewBox="0 0 16 16"
                                xmlns="http://www.w3.org/2000/svg"
                            >
                                <path
                                    d="M8 6.586L5.879 4.464 4.464 5.88 6.586 8l-2.122 2.121 1.415 1.415L8 9.414l2.121 2.122 1.415-1.415L9.414 8l2.122-2.121-1.415-1.415L8 6.586zM8 16A8 8 0 1 1 8 0a8 8 0 0 1 0 16z"
                                    fill-rule="evenodd"
                                ></path>
                            </svg>
                        </button>
                    </div>
                </div>

                <div class="bx--toolbar-content">
                    <button
                        v-if="hasActionButton"
                        class="bx--btn bx--btn--sm bx--btn--primary"
                        :disabled="isLoading"
                    >
                        {{actionButtonLabel}}
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
                    <slot></slot>
                </div>
            </section>
            <table class="bx--data-table-v2">
                <thead>
                    <tr class="bx--row">
                        <template v-if="isLoading">
                            <th v-for="i in 3" :key="i">
                                <button class="bx--skeleton__text"></button>
                            </th>
                        </template>
                        <template v-else>
                            <th v-for="(header, index) in headers" :key="index">
                                <button
                                    :id="'btn-sort-' + index"
                                    class="bx--table-sort-v2"
                                    data-event="sort"
                                >
                                    <span class="bx--table-header-label">{{header.title}}</span>
                                    <svg
                                        class="bx--table-sort-v2__icon"
                                        width="10"
                                        height="5"
                                        viewBox="0 0 10 5"
                                        aria-label="Sort rows by this header in descending order"
                                        alt="Sort rows by this header in descending order"
                                    >
                                        <title>Sort rows by this header in descending order</title>
                                        <path d="M0 0l5 4.998L10 0z" fill-rule="evenodd"></path>
                                    </svg>
                                </button>
                            </th>
                            <th class="last-column" v-if="!disableActions">Actions</th>
                        </template>
                    </tr>
                </thead>
                <tbody>
                    <template v-if="isLoading">
                        <tr v-for="tri in 5" :key="tri">
                            <td v-for="tdi in 3" :key="tdi"></td>
                        </tr>
                    </template>
                    <template v-else>
                        <tr
                            class="bx--row"
                            v-for="(row, rowIndex) in displayedRows"
                            :key="rowIndex"
                        >
                            <td
                                v-for="(cell, cellIndex) in row.data"
                                :key="cellIndex"
                            >{{cell | striphtml}}</td>
                            <td v-if="!disableActions">
                                <span class="bx--table-actions">
                                    <button
                                        v-if="canDeactivate"
                                        class="bx--toolbar-action"
                                        @click="$emit('deactivate-click', row)"
                                    >
                                        <svg
                                            class="bx--toolbar-action__icon"
                                            fill-rule="evenodd"
                                            height="16"
                                            name="edit"
                                            role="img"
                                            viewBox="0 0 16 16"
                                            width="16"
                                            aria-label="Deactivate"
                                            alt="Deactivate"
                                        >
                                            <title>Deactivate</title>
                                            <path
                                                d="M2.751 4.165a6.5 6.5 0 0 0 9.084 9.084L2.75 4.165zm1.414-1.414l9.084 9.084A6.5 6.5 0 0 0 4.165 2.75zM8 16A8 8 0 1 1 8 0a8 8 0 0 1 0 16z"
                                            ></path>
                                        </svg>
                                    </button>
                                    <button
                                        class="bx--toolbar-action"
                                        @click="$emit('edit-click', row)"
                                    >
                                        <svg
                                            class="bx--toolbar-action__icon"
                                            fill-rule="evenodd"
                                            height="16"
                                            name="edit"
                                            role="img"
                                            viewBox="0 0 16 16"
                                            width="16"
                                            aria-label="Edit"
                                            alt="Edit"
                                        >
                                            <title>Edit</title>
                                            <path
                                                d="M7.926 3.38L1.002 9.72V12h2.304l6.926-6.316L7.926 3.38zm.738-.675l2.308 2.304 1.451-1.324-2.308-2.309-1.451 1.329zM.002 9.28L9.439.639a1 1 0 0 1 1.383.03l2.309 2.309a1 1 0 0 1-.034 1.446L3.694 13H.002V9.28zM0 16.013v-1h16v1z"
                                            ></path>
                                        </svg>
                                    </button>
                                </span>
                            </td>
                        </tr>
                    </template>
                </tbody>
            </table>
        </div>
        <div :id="pgUuid" class="bx--pagination" data-pagination>
            <div class="bx--pagination__left">
                <span class="bx--pagination__text">Items per page:</span>
                <div class="bx--select bx--select--inline">
                    <label
                        for="select-id-pagination-left"
                        class="bx--visually-hidden"
                    >Number of items per page</label>
                    <select
                        id="select-id-pagination-left"
                        class="bx--select-input"
                        data-items-per-page
                    >
                        <option class="bx--select-option" value="10" selected>10</option>
                        <option class="bx--select-option" value="20">20</option>
                        <option class="bx--select-option" value="30">30</option>
                        <option class="bx--select-option" value="40">40</option>
                        <option class="bx--select-option" value="50">50</option>
                        <option class="bx--select-option" :value="rows.length">all</option>
                    </select>
                    <svg class="bx--select__arrow" width="10" height="5" viewBox="0 0 10 5">
                        <path d="M0 0l5 4.998L10 0z" fill-rule="evenodd"></path>
                    </svg>
                </div>
                <span class="bx--pagination__text">
                    <span>|</span>
                    <span data-displayed-item-range>{{displayRange.from}} - {{displayRange.to}}</span> of
                    <span data-total-items>{{numberOfItems}}</span> items
                </span>
            </div>
            <div class="bx--pagination__right bx--pagination--inline">
                <span class="bx--pagination__text">
                    <span data-displayed-page-number>{{currentPage}}</span> of
                    <span data-total-pages>{{numberOfPages}}</span> pages
                </span>
                <button
                    class="bx--btn bx--pagination__button bx--pagination__button--backward"
                    data-page-backward
                    aria-label="Backward button"
                    :disabled="currentPage === 1"
                >
                    <svg
                        class="bx--pagination__button-icon"
                        width="7"
                        height="12"
                        viewBox="0 0 7 12"
                    >
                        <path
                            fill-rule="nonzero"
                            d="M1.45 6.002L7 11.27l-.685.726L0 6.003 6.315 0 7 .726z"
                        ></path>
                    </svg>
                </button>
                <label class="bx--visually-hidden">Page number input</label>
                <div class="bx--select bx--select--inline">
                    <label
                        for="select-id-pagination-right"
                        class="bx--visually-hidden"
                    >Number of items per page</label>
                    <select
                        id="select-id-pagination-right"
                        class="bx--select-input"
                        data-page-number-input
                    >
                        <option
                            class="bx--select-option"
                            v-for="i in numberOfPages"
                            :key="i"
                            :value="i"
                            :selected="i === currentPage"
                        >{{i}}</option>
                    </select>
                    <svg class="bx--select__arrow" width="10" height="5" viewBox="0 0 10 5">
                        <path d="M0 0l5 4.998L10 0z" fill-rule="evenodd"></path>
                    </svg>
                </div>
                <button
                    class="bx--btn bx--pagination__button bx--pagination__button--forward"
                    data-page-forward
                    aria-label="Forward button"
                    :disabled="currentPage === numberOfPages"
                >
                    <svg
                        class="bx--pagination__button-icon"
                        width="7"
                        height="12"
                        viewBox="0 0 7 12"
                    >
                        <path
                            fill-rule="nonzero"
                            d="M5.569 5.994L0 .726.687 0l6.336 5.994-6.335 6.002L0 11.27z"
                        ></path>
                    </svg>
                </button>
            </div>
        </div>
    </div>
</template>

<script>
import { DataTableV2, Pagination, Search } from "carbon-components";
import helpers from "../helpers/helpers";

export default {
    name: "MyDataTable",
    props: {
        headers: {
            type: Array,
            required: true
        },
        rows: {
            type: Array,
            required: true
        },
        actionButtonLabel: {
            type: String
        },
        isLoading: {
            type: Boolean
        },
        canDeactivate: {
            type: Boolean,
            default: false
        },
        disableActions: {
            type: Boolean,
            default: false
        },
        disableSearch: {
            type: Boolean,
            default: false
        },
        altSearchText: {
            type: Object,
            default: () => {
                return {};
            }
        }
    },
    mounted: function() {
        this.attachCarbonDataTableV2();
        this.attachCarbonPagination();
        this.attachCarbonSearch();
    },
    data() {
        return {
            currentPage: 1,
            itemsPerPage: 10,
            filteredRows: this.rows.slice(),
            dtUuid: "data-table-v2-" + helpers.getRandomUuid(),
            pgUuid: "data-pagination-" + helpers.getRandomUuid(),
            searchUuid: "data-search-" + helpers.getRandomUuid(),
            searchText: ""
        };
    },
    watch: {
        rows: function(val) {
            this.filteredRows = val.slice();
        },
        searchText: function(val) {
            this.search(val);
        },
        altSearchText: {
            handler: function(newQuery) {
                this.alternativeSearchFn(newQuery);
            },
            deep: true
        }
    },
    computed: {
        hasActionButton: function() {
            return (
                this.actionButtonLabel &&
                this.actionButtonLabel.trim().length > 0
            );
        },
        numberOfItems: function() {
            return this.filteredRows.length;
        },
        numberOfPages: function() {
            return Math.trunc(this.numberOfItems / this.itemsPerPage) + 1;
        },
        displayRange: function() {
            return {
                from: (this.currentPage - 1) * this.itemsPerPage + 1,
                to:
                    this.currentPage * this.itemsPerPage > this.numberOfItems
                        ? this.numberOfItems
                        : this.currentPage * this.itemsPerPage
            };
        },
        displayedRows: function() {
            return this.filteredRows.slice(
                this.displayRange.from - 1,
                this.displayRange.to
            );
        }
    },
    methods: {
        attachCarbonDataTableV2: function() {
            const dt = this.$el.querySelector("#" + this.dtUuid);
            DataTableV2.create(dt);
            dt.addEventListener("data-table-v2-aftertogglesort", this.onSort);
        },
        attachCarbonPagination: function() {
            const pg = this.$el.querySelector("#" + this.pgUuid);
            Pagination.create(pg);
            pg.addEventListener("pageChange", this.onPageNavigate);
            pg.addEventListener("pageNumber", this.onPageSelect);
            pg.addEventListener("itemsPerPage", this.onItemsPerPageChange);
        },
        attachCarbonSearch: function() {
            const search = this.$el.querySelector("#" + this.searchUuid);
            Search.create(search);
        },
        customSorter: function(elIdx, asc) {
            // function generator, because sort function only takes 2 args
            const k = asc ? 1 : -1;
            const isNum = this.headers[elIdx].isNum;
            return function(arr1, arr2) {
                let a, b;
                if (isNum) {
                    // column contains only numbers as data
                    a = +arr1.data[elIdx];
                    b = +arr2.data[elIdx];
                } else {
                    // column contains strings only
                    a = arr1.data[elIdx].toLowerCase();
                    b = arr2.data[elIdx].toLowerCase();
                }
                return a < b ? -1 * k : a > b ? 1 * k : 0;
            };
        },
        onSort: function(event) {
            const isAscending = event.detail.element.className.includes(
                "ascending"
            );
            const columnIndex = +event.detail.element.id.replace(
                "btn-sort-",
                ""
            );
            this.filteredRows.sort(this.customSorter(columnIndex, isAscending));
        },
        onPageNavigate: function(event) {
            const dir = event.detail.direction === "forward" ? 1 : -1;
            if (dir === 1) {
                if (this.currentPage < this.numberOfPages) {
                    this.currentPage++;
                }
            } else {
                if (this.currentPage > 1) {
                    this.currentPage--;
                }
            }
        },
        onPageSelect: function(event) {
            this.currentPage = +event.detail.value;
        },
        onItemsPerPageChange: function(event) {
            this.itemsPerPage = +event.detail.value;
            this.currentPage = 1;
        },
        search: function(val) {
            const text = val.trim().toLowerCase();
            this.filteredRows = this.rows.filter(row =>
                row.data.some(cell => this.someSearchFilter(cell, text))
            );
        },
        alternativeSearchFn(q) {
            const query = Object.entries(q).reduce((r, pair) => {
                return [...r, ...pair[1]];
            }, []);
            if (query.length === 0) {
                this.filteredRows = this.rows;
                return;
            }
            this.filteredRows = this.rows.filter(row => {
                let pass = true;
                for (let i = 0; i < query.length; i++) {
                    pass = row.data.some(cell =>
                        cell != null
                            ? cell.toString().toLowerCase() === query[i]
                            : false
                    );
                    if (!pass) break;
                }
                return pass;
            });
        },
        someSearchFilter(cell, query) {
            return cell != null
                ? cell
                      .toString()
                      .toLowerCase()
                      .includes(query)
                : false;
        },
        onSearchClear() {
            this.searchText = "";
        }
    }
};
</script>

<style lang="scss" scoped>
input:disabled,
button:disabled {
    cursor: not-allowed !important;
}

svg {
    &.disabled {
        cursor: not-allowed;
    }
}

.bx--table-toolbar {
    padding-top: 0;
}

.bx--toolbar-search-container {
    border: 1px solid #dfe3e6;
    box-sizing: border-box;
}

.last-column {
    width: 3% !important;
}

tr.bx--row {
    display: table-row;
    margin: 0;
}

.bx--table-actions {
    display: flex;
    height: 100%;
    align-items: center;
}
</style>
