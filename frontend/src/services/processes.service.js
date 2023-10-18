import axios from "axios";

export const STATUS = {
    ACTIVE: "Active",
    INACTIVE: "Inactive"
};

/**
 * returns all processes specified for domain
 * @param {Int} domainId domain ID
 */
function getAll(domainId) {
    return axios
        .get(`${process.env.VUE_APP_API_URL}/processes?domainId=${domainId}`)
        .then(res => res.data);
}

/**
 * used to create a process
 * @param {Object} processData see swagger
 */
function createProcess(processData) {
    // console.log(processData);
    return axios
        .post(`${process.env.VUE_APP_API_URL}/processes/add`, processData)
        .then(res => res);
}

/**
 * used to update a process
 * @param {Object} processData see swagger
 */
function updateProcess(processData) {
    console.log('processData :', processData);
    return axios
        .put(`${process.env.VUE_APP_API_URL}/processes/update`, processData)
        .then(res => res);
}

function exportToExcel(parameters) {
    return axios
        .post(
            `${process.env.VUE_APP_API_URL}/processes/export`,
            parameters,
            {
                responseType: 'blob',
                timeout: 280000
            }
        )
        .then(res => {
            let blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8' });
            let url = window.URL.createObjectURL(blob);
            window.open(url);
        }).catch(error => console.error(error));
}

export const processesService = {
    getAll,
    createProcess,
    updateProcess,
    exportToExcel
};
