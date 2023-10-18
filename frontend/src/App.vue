<template>
    <div id="app">
        <spinner/>
        <MyHeader></MyHeader>
        <div class="content-wrapper">
            <MySidebar></MySidebar>
            <MyContent></MyContent>
        </div>
        <container></container>
    </div>
</template>

<script>
import Spinner from "./components/Spinner";
import MyHeader from "./components/MyHeader";
import MySidebar from "./components/MySidebar";
import MyContent from "./components/MyContent";
import Container from "./components/Notifications/Container";

import axios from "axios";
import { mapState } from "vuex";

export default {
    name: "App",
    components: {
        Spinner,
        MyHeader,
        MySidebar,
        MyContent,
        Container
    },
    computed: {
        ...mapState({
            user: state => state.user.user
        }),
        authenticated: function() {
            const user = this.$store.getters["user/getUser"];
            return user.token != null;
            // return sessionStorage.getItem('token') !== undefined;
        }
    },
    watch: {
        $route(to, from) {
            // eslint-disable-line
            this.checkAuthentication();
        }
    },
    created: function() {},
    mounted: function() {
        this.checkAuthentication();
    },
    methods: {
        checkAuthentication() {
            if (!this.authenticated) {
                if (!this.$cookies.isKey("srapusr")) {
                    // if user not logged redirect to login
                    this.$router.push("/login");
                } else {
                    const user = this.$cookies.get("srapusr");
                    if (sessionStorage.getItem("token") !== undefined) {
                        // if BE token not expired prolong login for 24h
                        this.$cookies.set("srapusr", user, "1d");
                    } else {
                        this.$cookies.remove("srapusr");
                        this.$router.push("/login");
                    }
                    sessionStorage.setItem("token", user.token);
                    axios.defaults.headers.common["Authorization"] = `Bearer ${
                        user.token
                    }`;
                    this.$store.dispatch("user/previouslyLoged", user);
                }
            }
        }
    }
};
</script>

<style lang="scss">
@import "./assets/scss/main";

#app {
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    .content-wrapper {
        height: calc(100% - 60px);

        #my-sidebar {
            float: left;
            height: 100%;
        }

        #my-content {
            height: 100%;
        }
    }

    .bx--tag--custom {
        background-color: #f5a0a9;
    }
}
</style>
