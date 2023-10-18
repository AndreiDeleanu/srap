<template>
    <div class="whiteScreen">
        <div class="loginTile">
            <h2>Welcome to SRAP</h2>
            <div>
                <div class="bx--form-item">
                    <input
                        class="bx--text-input"
                        id="username"
                        placeholder="Email"
                        type="text"
                        v-model="user.username"
                        @keyup.enter="login"
                    >
                </div>
                <div class="bx--form-item">
                    <input
                        class="bx--text-input"
                        id="password"
                        placeholder="W3ID Password"
                        type="password"
                        v-model="user.password"
                        @keyup.enter="login"
                    >
                </div>
            </div>
            <div class="action">
                <button
                    :disabled="loginLoading"
                    @click="login"
                    class="bx--btn bx--btn--primary"
                    type="button"
                >Login</button>
                <div class="bx--loading bx--loading--small" v-if="loginLoading && triedToLogIn">
                    <svg class="bx--loading__svg" viewBox="-75 -75 150 150">
                        <title>Loading</title>
                        <circle cx="0" cy="0" r="37.5"></circle>
                    </svg>
                </div>
            </div>
            <p>Please login using your w3id credentials.</p>
        </div>
    </div>
</template>

<script>
    import {mapState} from 'vuex';

    export default {
        name: 'Login',
        data: () => {
            return {
                user: {
                    username: '',
                    password: ''
                },
                disabled: false,
                triedToLogIn: false,
                navigatedFrom: {
                    fullPath: '/'
                }
            }
        },
        // mounted: function() {
        //   	if (sessionStorage.getItem('token') !== undefined) {
        //   	    this.$router.replace("/");
        // 	}
        // },
        computed: {
            ...mapState({
                loginLoading: state => state.user.isLoading
            })
        },
        methods: {
            login: function () {
                this.triedToLogIn = true;
                this.disabled = true;
                this.$store.dispatch('user/login', {
                    user: this.user,
                    redirect: this.navigatedFrom.fullPath
                });
            }
        },
        beforeRouteEnter(to, from, next) {
            next(vm => {
                vm.navigatedFrom = from;
            })
        }
    }
</script>

<style lang="scss" scoped>
    .whiteScreen {
        display: flex;
        position: fixed;
        top: 0;
        right: 0;
        height: 100vh;
        width: 100vw;
        background: #e0e0e0;
        justify-content: space-around;
        align-items: center;

        .loginTile {
            display: flex;
            flex-flow: column nowrap;
            width: 30%;
            min-width: 350px;
            min-height: 350px;
            background: white;
            border: 1px #eee solid;
            justify-content: space-between;
            align-items: center;
            padding: 1.5rem;
            box-shadow: 0 3px 5px 0 rgba(0, 0, 0, 0.158);
        }
    }

    .bx--form-item {
        margin-bottom: 1rem;
    }

    .action {
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>
