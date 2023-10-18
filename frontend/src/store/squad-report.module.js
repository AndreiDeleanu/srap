import { squadReportService } from "../services/squad-report.service";
import { helpers } from "../helpers/helpers";

const state = {
    isLoading: false,
    forceLoadingState: false,
    data: {
        squadId: -1,
        fllRating: "",
        calculatedRating: 0.0,
        fllComment: "",
        quarter: "",
        status: "Draft",
        reportId: -1,
        squadName: "",
        fpName: "",
        fllName: "",
        processes: [
            {
                processId: 0,
                rating: "",
                comment: ""
            }
        ]
    },
    reports: []
};

const actions = {
    get({ commit }, squadId) {
        commit("isLoading", true);
        squadReportService
            .get(squadId)
            .then(sr => commit("getSuccess", sr))
            .then(() => commit("isLoading", false))
            .catch(() => commit("isLoading", false));
    },
    updateProcess({ commit }, updatedProc) {
        commit("updateProcess", updatedProc);
    },
    save({ commit, dispatch }, updatedReport) {
        commit("isLoading", true);
        squadReportService
            .save(updatedReport)
            .then(() => {
                commit("save", updatedReport);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Report updated!",
                        subtitle: "",
                        caption: "",
                        severity: "success"
                    },
                    { root: true }
                );
                dispatch("get", updatedReport.squadId);
            })
            // .then(() => commit("forceLoadingState", false))
            .catch(() => commit("isLoading", false));
    },
    submit({ commit, dispatch }, updatedReport) {
        commit("isLoading", true);
        squadReportService
            .submit(updatedReport)
            .then(() => {
                commit("submit", updatedReport);
                dispatch(
                    "notifications/createNotification",
                    {
                        title: "Report updated!",
                        subtitle: "",
                        caption: "",
                        severity: "success"
                    },
                    { root: true }
                );
                if (updatedReport.status === "Signed") {
                    dispatch(
                        "emails/approved",
                        {
                            reportId: updatedReport.reportId,
                            reportName: updatedReport.squadName,
                            recipients: updatedReport.fpName
                        },
                        { root: true }
                    );
                } else {
                    dispatch(
                        "emails/submitted",
                        {
                            reportId: updatedReport.reportId,
                            reportName: updatedReport.squadName,
                            recipients: updatedReport.fllName
                        },
                        { root: true }
                    );
                }
                dispatch("get", updatedReport.squadId);
            })
            // .then(() => commit("forceLoadingState", false))
            .catch(e => {
                console.warn(e);
                commit("isLoading", false);
            });
    },
    squadStepBackToFP({ commit, dispatch }, data) {
        commit("forceLoadingState", true);
        squadReportService
            .stepBackToFP(data.reportId)
            .then(() => {
                dispatch("getSpecificReport", data.reportId);
                dispatch(
                    "emails/stepBackEmail",
                    {
                        reportId: data.reportId,
                        reportName: data.reportName,
                        stepTo: "Draft",
                        recipients: data.recipients
                    },
                    { root: true }
                );
            })
            .catch(e => {
                console.warn(e);
                commit("forceLoadingState", false);
            });
    },
    stepBack({ commit, dispatch }, data) {
        commit("forceLoadingState", true);
        squadReportService
            .stepBack(data)
            .then(() => {
                dispatch("getSpecificReport", data.reportId);
                dispatch(
                    "emails/stepBackEmail",
                    {
                        reportId: data.reportId,
                        reportName: data.reportName,
                        stepTo: data.step,
                        recipients: data.recipients
                    },
                    { root: true }
                );
            })
            .catch(e => {
                console.warn(e);
                commit("forceLoadingState", false);
            });
    },
    getReportsListByQuarter({ commit }, data) {
        commit("isLoading", true);
        squadReportService
            .getReportsListByQuarter(data)
            .then(sr => commit("storeReports", sr))
            .then(() => commit("isLoading", false))
            .catch(() => commit("isLoading", false));
    },
    getSpecificReport({ commit }, id) {
        commit("isLoading", true);
        squadReportService
            .getSpecificReport(id)
            .then(sr => commit("getSuccess", sr))
            .then(() => commit("isLoading", false))
            .catch(() => commit("isLoading", false));
    }
};

const mutations = {
    isLoading(state, val) {
        state.isLoading = val;
    },
    forceLoadingState(state, val) {
        state.forceLoadingState = val;
    },
    getSuccess(state, report) {
        if (report === undefined) {
            report = {
                id: null,
                fllRating: "",
                fllComment: "",
                quarter: helpers.getCurrentQYYYY(),
                processes: [],
                status: "Draft" // set status as new (default)
            };
        }
        if (report.fllRating == null) report.fllRating = "";
        state.data = report;
    },
    updateProcess(state, updatedProc) {
        let proc = state.data.processes.find(
            item => item.processId === updatedProc.processId
        );
        if (proc !== undefined) {
            proc = updatedProc;
        } else {
            state.data.processes.push(updatedProc);
        }
    },
    save(state, updatedReport) {
        state.data = updatedReport;
    },
    submit(state, updatedReport) {
        state.data = updatedReport;
    },
    storeReports(state, reports) {
        state.reports = reports.data;
    }
};

export const squadReport = {
    namespaced: true,
    state,
    actions,
    mutations
};
