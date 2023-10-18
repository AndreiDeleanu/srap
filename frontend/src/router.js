import Vue from "vue";
import Router from "vue-router";
import Dashboard from "./views/Dashboard";
import Admin from "./views/Admin";
import DomainReports from "./views/DomainReports"; // eslint-disable-line
import DomainReport from "./views/DomainReport";
import SquadsReports from "./views/SquadsReports";
import SquadReport2 from "./views/SquadReport2";
import SubdomainsReports from "./views/SubdomainsReports";
import History from "./views/History";
import PageNotFound from "./views/PageNotFound";
import Login from "./views/Login";
import SubdomainReport from "./views/SubdomainReport";
import Delegations from "./views/Delegations";
import Unauthorised from "./views/Unauthorised";

Vue.use(Router);

export const ROUTES = {
    DASHBOARD: {
        key: "Dashboard",
        path: "/:domain",
        icon: "chart-area",
        showInMenu: true
    },
    ADMIN: {
        key: "Admin",
        path: "/admin/:domain",
        icon: "sliders-h",
        showInMenu: true
    },
    DOMAIN_REPORTS: {
        key: "Domain Report",
        path: "/domain-reports/:domain",
        icon: "city",
        showInMenu: true
    },
    SUBDOMAINS_REPORTS: {
        key: "Subdomains Reports",
        path: "/subdomains-reports/:domain",
        icon: "building",
        showInMenu: true
    },
    SQUADS_REPORTS: {
        key: "Squads Reports",
        path: "/squads-reports/:domain",
        icon: "campground",
        showInMenu: true
    },
    HISTORY: {
        key: "History",
        path: "/history/:domain",
        icon: "history",
        showInMenu: true
    },
    SQUAD_REPORT: { key: "Squad Report", path: "/squads-reports/sr/:id" },
    SUBDOMAIN_REPORT: {
        key: "Subdomain Report",
        path: "/subdomains-reports/sr/:id"
    },
    // DOMAIN_REPORT: { key: 'Domain Report', path: '/domain'},
    LOGIN: { key: "Login", path: "/login" },
    DELEGATIONS: {
        key: "Delegations",
        path: "/delegations",
        icon: "users-cog",
        showInMenu: true
    },
    UNAUTHORISED: { key: "Unauthorised", path: "/unauthorised" }
};

export default new Router({
    routes: [
        // {
        //   path: '/',
        //   name: 'home',
        //   component: Home
        // },
        // {
        //   path: '/about',
        //   name: 'about',
        //   // route level code-splitting
        //   // this generates a separate chunk (about.[hash].js) for this route
        //   // which is lazy-loaded when the route is visited.
        //   component: () => import(/* webpackChunkName: "about" */ './views/About.vue')
        // }
        // { path: '/', component: SquadsReports },
        { path: ROUTES.LOGIN.path, component: Login },
        { path: ROUTES.ADMIN.path, component: Admin },
        { path: ROUTES.DOMAIN_REPORTS.path, component: DomainReport },
        { path: ROUTES.SUBDOMAINS_REPORTS.path, component: SubdomainsReports },
        { path: ROUTES.SUBDOMAIN_REPORT.path, component: SubdomainReport },
        { path: ROUTES.SQUADS_REPORTS.path, component: SquadsReports },
        { path: ROUTES.SQUAD_REPORT.path, component: SquadReport2 },
        { path: ROUTES.HISTORY.path, component: History },
        { path: ROUTES.DELEGATIONS.path, component: Delegations },
        { path: ROUTES.UNAUTHORISED.path, component: Unauthorised },
        { path: ROUTES.DASHBOARD.path, component: Dashboard },
        { path: "**", component: PageNotFound }
    ]
});
