<template>
    <div class="bx--tile">
        <tabs :sections="tabs">
            <template :slot="'slot-' + tabs[0]">
                <my-data-table
                    :headers="headers"
                    :rows="tableData"
                    :is-loading="isLoading"
                    @edit-click="subdomainReportEdit"
                ></my-data-table>
            </template>
            <template :slot="'slot-' + tabs[1]">
                <my-data-table
                    :headers="headers"
                    :rows="delegatedTableData"
                    :is-loading="isLoading"
                    @edit-click="subdomainReportEdit"
                ></my-data-table>
            </template>
        </tabs>
    </div>
</template>

<script>
    import {mapActions, mapState} from 'vuex';
    import MyDataTable from '../components/MyDataTable'
    import Tabs from '../components/Tabs';

    export default {
        name: "SubdomainsReports",
        components: {
            MyDataTable,
            Tabs
        },
        computed: {
            ...mapState({
                isLoading: state => state.subdomainReport.isLoading,
                subdomainsReportss: state=> state.subdomainReport.subdomainsReports,
                subdomains: state => state.subdomains.subdomains,
                user: state => state.user.user,
            }),
            tableData() {
                const rows = this.subdomains.reduce((r,sdomain) => {
                    if (
                        (this.user.roles.indexOf('DEV_ADMIN') >= 0
                        || this.user.roles.indexOf('DOMAIN_ADMIN') >= 0
                        || sdomain.sllName.toLowerCase() === this.user.user.toLowerCase())
                        && sdomain.status === 'Active'
                    ) {
                        return [...r, {
                            data: [
                                sdomain.name,
                                sdomain.sllName,
                            ],
                            metadata: {
                                id: sdomain.id
                            }
                        }];
                    } else {
                        return r;
                    }
                }, []);
                return rows;
            },
            delegatedTableData() {
                const delegators = this.user.delegations.reduce((r,d) => {
                    if (d.role === 'SLL') return [...r, d.delegatorEmail];
                    return r;
                }, []);
                const rows = this.subdomains.reduce((r,sdomain) => {
                    if (
                        (this.user.delegatedRoles.indexOf('DEV_ADMIN') >= 0
                        || this.user.delegatedRoles.indexOf('DOMAIN_ADMIN') >= 0
                        || delegators.includes(sdomain.sllName.toLowerCase()))
                        && sdomain.status === 'Active'
                    ) {
                        return [...r, {
                            data: [
                                sdomain.name,
                                sdomain.sllName
                            ],
                            metadata: {
                                id: sdomain.id
                            }
                        }];
                    } else {
                        return r;
                    }
                }, []);
                return rows;
            }
        },
        data() {
            return {
                tabs: [
                    'My Subdomains',
                    'Delegated Subdomains'
                ],
                headers: [
                    {title: 'Name'},
                    {title: 'Owner'}
                ]
            }
        },
		created: function() {
            this.setContentTitle(`Subdomain Reports`);
            // this.$store.dispatch('subdomainReport/getAll', this.$route.params.domain);
            this.$store.dispatch('subdomains/getAll', this.$route.params.domain);
		},
		methods: {
            ...mapActions('myContent', {
                setContentTitle: 'setTitle'
            }),
            subdomainReportEdit(subdomain) {
                // console.log(subdomain)
                this.$router.push('/subdomains-reports/sr/' + subdomain.metadata.id);
            }
		}
    }
</script>

<style lang="scss" scoped>
</style>
