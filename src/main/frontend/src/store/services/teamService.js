import { delFactory, getFactory, putFactory } from './request';

const apiEndpoint = '/teams';

export const getTeamApi = id => getFactory(`${apiEndpoint}/${id}`);
export const removeTeamApi = id => delFactory(`${apiEndpoint}/${id}`);
export const updateTeamApi = teamObject => putFactory(apiEndpoint, teamObject);
