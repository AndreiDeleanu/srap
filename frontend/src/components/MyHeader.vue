<template>
    <div id="my-header">
        <div id="logo">
            <span class="bx--type-beta">SRAP - Security & Risk Assessment Portal</span>
        </div>
        <div class="right">
            <div class="help" v-if="domain.help != ''">
                <svg width="20" height="20" viewBox="0 0 20 20">
                    <path
                        d="M10 17a7 7 0 1 0 0-14 7 7 0 0 0 0 14zm0 1a8 8 0 1 1 0-16 8 8 0 0 1 0 16z"
                    ></path>
                    <path
                        d="M10.5 10.5V12h-1V9.5h1a1.5 1.5 0 0 0 0-3h-1A1.5 1.5 0 0 0 8 8H7a2.5 2.5 0 0 1 2.5-2.5h1a2.5 2.5 0 1 1 0 5zM9 14a1 1 0 1 1 2 0 1 1 0 1 1-2 0"
                    ></path>
                </svg>
                <a :href="helpURL">Help</a>
            </div>
            <div id="profile">
                <span class="bx--type-delta user-name">{{username}}</span>
                <span class="user-icon">
                    <img :src="user.userImageUrl" :alt="user.user">
                </span>
                <div class="userActions">
                    <span>
                        <h6>Roles</h6>
                    </span>
                    <span v-for="(role, index) in allRoles" :key="role + index">{{role}}</span>
                    <span @click="logout">Log Out
                        <font-awesome-icon icon="sign-out-alt"/>
                    </span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import MyDropdown from "../components/MyDropdown";
import { ROLES } from "../services/roles.service";
import { mapActions, mapState } from "vuex";

export default {
    name: "MyHeader",
    components: {
        MyDropdown
    },
    computed: {
        ...mapState({
            user: state => state.user.user,
            domain: state => state.domains.domain
        }),
        username() {
            if (this.user.user !== undefined) {
                try {
                    const first = this.user.user.split("@")[0];
                    return `Welcome, ${first}`;
                } catch (e) {
                    return "Welcome, sorry no access for you.";
                }
            }
            return "";
        },
        role() {
            return this.user.roles[0];
        },
        helpURL() {
            if (!!!this.domain.help) {
                return "";
            }
            return this.domain.help.indexOf("http") != 0
                ? "https://" + this.domain.help
                : this.domain.help;
        },
        allRoles() {
            const delegatedRoles = this.user.delegatedRoles ? this.user.delegatedRoles : [];
            const roles = this.user.roles ? this.user.roles : [];

            return [...roles, ...delegatedRoles];
        }
    },
    methods: {
        debugOnItemSelected(val) {
            this.debugSetUserRole(val);
        },
        logout() {
            this.$cookies.remove("srapusr");
            this.$store.dispatch("user/logout");
            this.$router.push("/login");
        }
    }
};
</script>

<style lang="scss" scoped>
#my-header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: #152935;
    color: #ffffff;

    #logo {
        margin: 0 12px;
        span {
            font-weight: 400;
        }
    }

    .right {
        display: flex;
        align-items: center;
        height: 100%;

        .help {
            margin-right: 2rem;
            color: white;
            display: flex;
            align-items: center;
            transition: color 300ms;

            &:hover {
                color: #498cf3;
            }

            a {
                color: inherit;
                text-decoration: none;
            }
            svg {
                fill: currentColor;
            }
        }

        #profile {
            display: flex;
            height: 100%;
            position: relative;
            padding-right: 12px;
            align-items: center;
            justify-content: space-between;
            cursor: default;

            .user-name {
                font-weight: 400;
                margin-right: 12px;
            }

            .user-icon {
                img {
                    height: 40px;
                    width: 40px;
                    border-radius: 35px;
                    border: white 2px solid;
                }
            }

            .userActions {
                display: none;
                background: white;
                color: #152935;
                position: absolute;
                right: 0;
                bottom: 0;
                transform: translateY(100%);
                z-index: 10;
                box-shadow: 0 5px 5px 0 rgba(0, 0, 0, 0.11);

                span {
                    display: block;
                    height: 30px;
                    padding: 0 1.5rem;
                    line-height: 30px;

                    h6 {
                        line-height: 30px;
                    }

                    &:last-of-type {
                        height: 60px;
                        line-height: 60px;
                        border-top: 1px solid grey;
                        cursor: pointer;
                        transition: background 150ms, color 150ms;

                        &:hover {
                            background: #769db5;
                            color: white;
                        }
                    }
                }
            }

            &:hover {
                .userActions {
                    display: block;
                }
            }
        }
    }
}

@media print {
    #my-header {
        display: none;
    }
}
</style>


