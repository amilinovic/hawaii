import { delFactory, getFactory, postFactory } from './request';

const apiEndpoint = '/leaveprofiles';

export const getLeaveProfileApi = id => getFactory(`${apiEndpoint}/${id}`);
export const removeLeaveProfileApi = id => delFactory(`${apiEndpoint}/${id}`);
export const createLeaveProfileApi = leaveProfileObject =>
  postFactory(apiEndpoint, leaveProfileObject);
