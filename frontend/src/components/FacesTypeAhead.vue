<template>
    <div class="facesTypeAhead">
        <div class="input_wrapper">
            <label :for="inputId" class="bx--label">{{label}}</label>
            <input
                :id="inputId"
                type="text"
                class="bx--text-input"
                :value="value"
                @input="input"
                @blur="close"
            >
        </div>
        <div
            class="sugestions"
            :class="{'show': showSugestions, 'one': oneResult, 'two': twoResults}"
        >
            <div v-if="isLoading" class="loading">Loading ...</div>
            <div
                v-else
                v-for="user in users"
                :key="user.email"
                @click="pickuser(user)"
                class="user_wrapper"
            >
                <div class="user_photo">
                    <img :src="user.photo">
                </div>
                <div class="user_info">
                    <p>{{user.fullName}}</p>
                    <p>
                        <strong>{{user.email}}</strong>
                    </p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { mapState } from "vuex";
// import helpers from '../helpers/helpers.js';
import { debounce } from "debounce";
export default {
    name: "FacesTypeAhead",
    props: {
        label: {
            type: String,
            default: "Bluepages Face typeahead"
        },
        value: {
            type: String,
            default: ""
        }
    },
    computed: {
        ...mapState({
            users: state => state.BP.users,
            isLoading: state => state.BP.isLoading
        }),
        inputId() {
            return "faces-typeahead-input-" + this._uid;
        }
    },
    data: () => {
        return {
            showSugestions: false,
            oneResult: true,
            twoResults: false
        };
    },
    watch: {
        users(users) {
            this.oneResult = users.length <= 1 || this.isLoading;
            this.twoResults = users.length === 2;
        }
    },
    methods: {
        input: function(e) {
            this.$emit("update", e.target.value);
            this.tick(e);
        },
        tick: debounce(function(e) {
            this.callBack(e.target.value);
        }, 300),
        close() {
            this.showSugestions = false;
        },
        callBack(value) {
            if (value.length > 2) {
                this.showSugestions = true;
                this.$store.dispatch("BP/getSugestions", value);
            }
            if (value.length === 0) {
                this.showSugestions = false;
            }
        },
        pickuser(user) {
            this.showSugestions = false;
            this.$emit("update", user.email.toLowerCase());
        }
    }
};
</script>

<style lang="scss" scoped>
.facesTypeAhead {
    display: block;
    width: 100%;
    position: relative;

    .input_wrapper {
        width: 100%;
    }

    .sugestions {
        width: calc(35% - 6rem);
        height: 0px;
        position: fixed;
        background: white;
        overflow-y: auto;
        overflow-x: hidden;
        transition: height 250ms ease-in-out 100ms;
        z-index: 15;

        &.show {
            height: 197px;
            border: 1px solid #eee;
            box-shadow: 0 5px 5px 0 rgba(0, 0, 0, 0.1);
            &.one {
                height: 67px;
            }
            &.two {
                height: 132px;
            }
        }

        .user_wrapper {
            display: flex;
            flex-flow: row nowrap;
            height: 65px;
            align-items: center;
            cursor: pointer;
            transition: color 150ms, background 150ms;

            &:hover {
                color: white;
                background: lighten(#264a60, 20);
            }

            .user_photo {
                padding: 0 1rem;
                height: 45px;
                width: 45px;
                box-sizing: initial;

                img {
                    height: 45px;
                    width: 45px;
                    box-sizing: border-box;
                    border-radius: 45px;
                    border: 2px solid #152935;
                }
            }
        }

        .loading {
            line-height: 65px;
            font-size: 1.5rem;
            font-weight: 100;
            padding-left: 1.5rem;
        }
    }
}
</style>

