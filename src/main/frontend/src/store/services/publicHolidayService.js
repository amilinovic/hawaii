import { delFactory, getFactory, postFactory, putFactory } from './request';

const apiEndpoint = '/publicholidays';

export const createTeamApi = teamObject => postFactory(apiEndpoint, teamObject);
export const getPublicHolidayApi = id => getFactory(`${apiEndpoint}/${id}`);
export const removeTeamApi = id => delFactory(`${apiEndpoint}/${id}`);
export const updateTeamApi = teamObject => putFactory(apiEndpoint, teamObject);
