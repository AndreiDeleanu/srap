import Vue from "vue";
import Vuex from "vuex";
import { squads } from "./squads.module";
import { squadReport } from "./squad-report.module";
import { processes } from "./processes.module";
import { user } from "./user.module";
import { subdomains } from "./subdomains.module";
import { myContent } from "./my-content.module";
import { notifications } from "./notifications.module";
import { domains } from "./domains.module";
import { pdf } from "./pdf.module";
import { spinner } from "./spinner.module";
import { BP } from "./BP.module";
import { subdomainReport } from "./subdomain-report.module";
import { delegations } from "./delegations.module";
import { deadlines } from "./deadlines.module";
import { emails } from "./emails.module";
import { dashboardModule } from "./dashboard.module";
import { thresholdsModule } from "./thresholds.module";

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        squads,
        squadReport,
        processes,
        user,
        subdomains,
        myContent,
        notifications,
        domains,
        pdf,
        spinner,
        BP,
        subdomainReport,
        delegations,
        deadlines,
        emails,
        dashboardModule,
        thresholdsModule
    }
});
