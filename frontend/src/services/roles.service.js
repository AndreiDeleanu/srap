import { ROUTES } from "../router";

export const ROLES = {
    FP: "FP",
    FLL: "FLL",
    SLL: "SLL",
    DOMAIN_OWNER: "DOMAIN_OWNER",
    DOMAIN_ADMIN: "DOMAIN_ADMIN",
    SUPER_ADMIN: "SUPER_ADMIN",
    DEV_ADMIN: "DEV_ADMIN"
};

const CONFIG = {
    FP: {
        sections: [
            ROUTES.DASHBOARD.key,
            ROUTES.SQUADS_REPORTS.key,
            ROUTES.SQUAD_REPORT.key,
            ROUTES.HISTORY.key,
            ROUTES.DELEGATIONS.key
        ],
        level: 0,
        isInclusive: false
    },
    FLL: {
        sections: [
            ROUTES.DASHBOARD.key,
            ROUTES.SQUADS_REPORTS.key,
            ROUTES.SQUAD_REPORT.key,
            ROUTES.HISTORY.key,
            ROUTES.DELEGATIONS.key
        ],
        level: 1,
        isInclusive: true
    },
    SLL: {
        sections: [
            ROUTES.DASHBOARD.key,
            ROUTES.SUBDOMAINS_REPORTS.key,
            ROUTES.SQUADS_REPORTS.key,
            ROUTES.SQUAD_REPORT.key,
            ROUTES.HISTORY.key,
            ROUTES.DELEGATIONS.key
        ],
        level: 2,
        isInclusive: true
    },
    DOMAIN_OWNER: {
        sections: [
            ROUTES.DASHBOARD.key,
            ROUTES.DOMAIN_REPORTS.key,
            ROUTES.SQUADS_REPORTS.key,
            ROUTES.SQUAD_REPORT.key,
            ROUTES.HISTORY.key,
            ROUTES.DELEGATIONS.key
        ],
        level: 3,
        isInclusive: true
    },
    DOMAIN_ADMIN: {
        sections: [
            ROUTES.DASHBOARD.key,
            ROUTES.SUBDOMAINS_REPORTS.key,
            ROUTES.DOMAIN_REPORTS.key,
            ROUTES.ADMIN.key,
            ROUTES.SQUADS_REPORTS.key,
            ROUTES.SQUAD_REPORT.key,
            ROUTES.HISTORY.key,
            ROUTES.DELEGATIONS.key
        ],
        level: 4,
        isInclusive: true
    },
    SUPER_ADMIN: {
        sections: [ROUTES.ADMIN.key, ROUTES.DELEGATIONS.key],
        level: 5,
        isInclusive: false
    },
    DEV_ADMIN: {
        sections: [
            ROUTES.DASHBOARD.key,
            ROUTES.SUBDOMAINS_REPORTS.key,
            ROUTES.DOMAIN_REPORTS.key,
            ROUTES.ADMIN.key,
            ROUTES.SQUADS_REPORTS.key,
            ROUTES.SQUAD_REPORT.key,
            ROUTES.HISTORY.key,
            ROUTES.DELEGATIONS.key
        ]
    }
};

/**
 *
 * @param {*} roles
 */
function getSections(roles) {
    let sections = [];
    roles.forEach(userRole => {
        sections = [...sections, ...CONFIG[userRole].sections];
    });
    return sections;
}

function isDev(roles) {
    return !!roles[ROLES.DEV_ADMIN];
}

function canAccessSection(roles, section) {
    return isDev(roles) || !!getSections(roles).includes(section);
}

export const rolesService = {
    getSections,
    canAccessSection
};
