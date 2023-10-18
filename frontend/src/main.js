import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from "./store/store";
import axios from "axios";
import VueCookies from 'vue-cookies';
import { library } from '@fortawesome/fontawesome-svg-core';
import {
    faBuilding,
    faCampground,
    faChartArea,
    faChevronLeft,
    faCity,
    faHistory,
    faSlidersH,
    faSignOutAlt,
    faUsersCog
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

// inject Font Awesome
library.add(faChartArea, faSlidersH, faCity, faBuilding, faCampground, faHistory, faChevronLeft, faSignOutAlt, faUsersCog);
Vue.component('font-awesome-icon', FontAwesomeIcon);

// inject Trumbowyg
window.jQuery = require('jquery');
require('trumbowyg');

const token = sessionStorage.getItem('token');
if (token) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

Vue.config.productionTip = false;

Vue.filter('striphtml', function (value) {
    var div = document.createElement("div");
    div.innerHTML = value;
    var text = div.textContent || div.innerText || "";
    return text;
});

Vue.use(VueCookies)

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app');
