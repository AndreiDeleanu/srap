<template>
    <div :class="{expanded: state === 'expanded'}" id="my-sidebar">
        <ul>
            <router-link
                :key="item.key"
                :to="item.path.replace(':domain', domainId)"
                class="bx--type-gamma"
                tag="li"
                v-for="item in navItems"
            >
                <font-awesome-icon :icon="item.icon" class="item-icon" fixed-width></font-awesome-icon>
                <span class="item-label">{{item.key}}</span>
            </router-link>
        </ul>
        <div class="toggle">
            <button @click="onToggle()" class="bx--type-gamma">
                <font-awesome-icon
                    :class="{'rotate': state === 'collapsed'}"
                    class="toggle-icon"
                    fixed-width
                    icon="chevron-left"
                    ref="toggleIcon"
                ></font-awesome-icon>
                <span class="bx--type-gamma toggle-label">Collapse</span>
            </button>
        </div>
    </div>
</template>

<script>
import { ROUTES } from "../router";
import { rolesService } from "../services/roles.service";
import { mapState } from "vuex";

const STATE = {
    EXPANDED: "expanded",
    COLLAPSED: "collapsed"
};

export default {
    name: "MySidebar",
    computed: {
        ...mapState({
            user: state => state.user.user,
            domainId: state => state.user.user.domainId[0]
        }),
        combinedRoles() {
            const roles = !!this.user.roles ? this.user.roles : [];
            const delegatedRoles = !!this.user.delegatedRoles
                ? this.user.delegatedRoles
                : [];
            return [...roles, ...delegatedRoles];
        },
        items() {
            const reduced = Object.keys(ROUTES).reduce((result, keyName) => {
                if (
                    this.hasAccess(ROUTES[keyName].key) &&
                    ROUTES[keyName].showInMenu
                ) {
                    return [
                        ...result,
                        {
                            key: ROUTES[keyName].key,
                            path: ROUTES[keyName].path,
                            icon: ROUTES[keyName].icon
                        }
                    ];
                } else {
                    return result;
                }
            }, []);
            return reduced;
        }
    },
    data() {
        return { navItems: [], state: STATE.EXPANDED };
    },
    watch: {
        state: function(newState) {
            localStorage.sidebarState = newState;
        },
        combinedRoles: {
            handler: function() {
                this.calculateItems();
            },
            deep: true
        }
    },
    mounted: function() {
        if (localStorage.sidebarState) {
            this.state = localStorage.sidebarState;
        }
        this.calculateItems();
    },
    methods: {
        isActive() {
            return this.$route.path;
        },
        onToggle() {
            if (this.state === STATE.EXPANDED) {
                this.state = STATE.COLLAPSED;
            } else {
                this.state = STATE.EXPANDED;
            }
        },
        hasAccess(section) {
            if (this.user === undefined || this.user.roles === undefined) {
                return false;
            }

            if (this.combinedRoles[0]) {
                return rolesService.canAccessSection(
                    this.combinedRoles,
                    section
                );
            } else {
                return false;
            }
        },
        calculateItems() {
            const reduced = Object.keys(ROUTES).reduce((result, keyName) => {
                if (
                    this.hasAccess(ROUTES[keyName].key) &&
                    ROUTES[keyName].showInMenu
                ) {
                    return [
                        ...result,
                        {
                            key: ROUTES[keyName].key,
                            path: ROUTES[keyName].path,
                            icon: ROUTES[keyName].icon
                        }
                    ];
                } else {
                    return result;
                }
            }, []);
            this.navItems = reduced;
        }
    }
};
</script>

<style lang="scss" scoped>
#my-sidebar {
    padding: 0;
    margin: 0;
    background: #264a60;
    color: #ffffff;
    display: flex;
    flex-direction: column;
    width: 55px;
    overflow: hidden;
    white-space: nowrap;
    transition: 300ms all;

    &.expanded {
        width: 250px;
    }

    ul {
        display: flex;
        flex-direction: column;
        width: 100%;
        padding: 0;
        margin: 0;
        list-style: none;

        li {
            width: 100%;
            padding: 12px;
            box-sizing: border-box;

            .item-icon {
                position: absolute;
                left: 15px;
                width: 25px;
            }

            .item-label {
                padding-left: 43px;
            }

            &.router-link-exact-active,
            &.router-link-active {
                border-left: 6px solid #5596e6;

                .item-label {
                    padding-left: 37px;
                }
            }

            &:hover {
                cursor: pointer;
                background: lighten(#264a60, 5%);
            }
        }
    }

    .toggle {
        margin-top: auto;

        button {
            border: none;
            background: transparent;
            color: white;
            outline: none;
            height: 50px;
            width: 100%;
            padding: 12px;
            cursor: pointer;
            display: flex;
            align-items: center;

            &:hover {
                background: lighten(#264a60, 5%);
            }

            .toggle-icon {
                position: absolute;
                left: 15px;
                width: 25px;
                transition: 300ms all;

                &.rotate {
                    transform: rotate(180deg);
                }
            }

            .toggle-label {
                padding-left: 43px;
            }
        }
    }
}

@media print {
    #my-sidebar {
        display: none;
    }
}
</style>
