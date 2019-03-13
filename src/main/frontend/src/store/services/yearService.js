import { delFactory, getFactory, postFactory, putFactory } from './request';

const apiEndpoint = '/years';

export const createYearApi = yearObject => postFactory(apiEndpoint, yearObject);
export const getTeamApi = id => getFactory(`${apiEndpoint}/${id}`);
export const removeTeamApi = id => delFactory(`${apiEndpoint}/${id}`);
export const updateTeamApi = teamObject => putFactory(apiEndpoint, teamObject);
