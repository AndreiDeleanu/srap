import { emailsService } from "../services/emails.service";

const state = {
    defaults: {
        bcc: [],
        cc: [],
        contact: "noreplySRAP.portal@ibm.com"
    },
    subjects: {
        RAJECTED: "Rejected : Your action is required at SRAP portal",
        SUBMITED: "Your action is required at SRAP portal",
        APPROVED: "Approved : No action is required at SRAP portal"
    }
};

const actions = {
    stepBackEmail({ commit }, data) {
        let message = "";
        switch (data.stepTo) {
            case "Draft":
                message = `<p>Dear Security Focal point your MSAC submission for squad <strong><a href='https://srap.w3ibm.mybluemix.net/srap/#/squads-reports/sr/${
                    data.reportId
                }'>${
                    data.reportName
                }</a></strong> got rejected by FLL. Please log-in to SRAP portal and make necessary updates before you submit again.</p>`;
                break;
            case "Submitted":
                message = `<p>Dear First Line Leader point your MSAC submission for squad <strong><a href='https://srap.w3ibm.mybluemix.net/srap/#/squads-reports/sr/${
                    data.reportId
                }'>${
                    data.reportName
                }</a></strong> got rejected by Domain Admin. Please log-in to SRAP portal and make necessary updates before you submit again.</p>`;
                break;

            default:
                break;
        }
        const email = {
            ...state.defaults,
            ...{
                message: message,
                subject: state.subjects.RAJECTED,
                recipients: [data.recipients]
            }
        };
        emailsService.sendEmail(email).then(r => {
            console.log("stepBack :", r);
        });
    },

    // eslint-disable-next-line
    submitted({ commit }, data) {
        const message = `<p>Dear FLL your action is needed. Please review MSAC submission from squad <strong><a href='https://srap.w3ibm.mybluemix.net/srap/#/squads-reports/sr/${
            data.reportId
        }'>${
            data.reportName
        }</a></strong>, fill in your final position and Sign-off the Report</p>`;
        const email = {
            ...state.defaults,
            ...{
                message: message,
                subject: state.subjects.SUBMITED,
                recipients: [data.recipients]
            }
        };
        emailsService.sendEmail(email).then(r => {
            console.log("submited :", r);
        });
    },

    // eslint-disable-next-line
    approved({ commit }, data) {
        const message = `<p>Dear Security Focal point your MSAC submission for squad <strong><a href='https://srap.w3ibm.mybluemix.net/srap/#/squads-reports/sr/"${
            data.reportId
        }'>${data.reportName}</a></strong> got approved by FLL.</p>`;
        const email = {
            ...state.defaults,
            ...{
                message: message,
                subject: state.subjects.APPROVED,
                recipients: [data.recipients]
            }
        };
        emailsService.sendEmail(email).then(r => {
            console.log("approved :", r);
        });
    }
};

export const emails = {
    namespaced: true,
    state,
    actions
};
