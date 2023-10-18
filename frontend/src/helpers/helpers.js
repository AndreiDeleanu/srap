import { PROCESS_RATING } from "../services/squad-report.service";
import moment from "moment";

function getRandomUuid() {
    const cryptoObj = window.crypto || window.msCrypto;
    return cryptoObj.getRandomValues(new Uint32Array(1))[0];
}

function calculateReportValue(processesDetails, reportProcesses) {
    let weight = 0;
    let score = 0;
    processesDetails.forEach(pd => {
        const rp = reportProcesses.find(item => pd.id === item.processId);
        if (rp !== undefined) {
            if (rp.rating !== PROCESS_RATING.NA) weight += +pd.weight;
        } else {
            weight += +pd.weight;
        }
        if (rp !== undefined) {
            if (rp.rating === PROCESS_RATING.SAT) {
                score += 3 * pd.weight;
            } else if (rp.rating === PROCESS_RATING.MARGINAL) {
                score += 2 * pd.weight;
            } else {
                // we add 0 * pd.weight to total score -- so we do nothing
            }
        }
    });
    const maxScore = weight * 3;
    return score / maxScore;
}

function getCurrentQYYYY() {
    const today = moment();
    const currentQYYYY = today.utc().quarter() + "Q" + today.format("YYYY");
    return currentQYYYY;
}

function getLastQuarters() {
    const today = moment();
    const quarters = [];
    for (let i = 0; i < 8; i++) {
        today.subtract(3, "months");
        quarters.push(today.utc().quarter() + "Q" + today.format("YYYY"));
    }
    return quarters;
}

function get3nextQuarters() {
    const today = moment();
    const quarters = [];
    for (let i = 0; i < 3; i++) {
        quarters.push(today.utc().quarter() + "Q" + today.format("YYYY"));
        today.add(3, "months");
    }
    return quarters;
}

export default {
    getRandomUuid,
    calculateReportValue,
    getCurrentQYYYY,
    getLastQuarters,
    get3nextQuarters
};
