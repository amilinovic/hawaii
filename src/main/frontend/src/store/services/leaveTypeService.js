import { delFactory, getFactory, postFactory, putFactory } from './request';

const apiEndpoint = '/leavetypes';

export const createLeaveTypeApi = leaveTypeObject =>
  postFactory(apiEndpoint, leaveTypeObject);
export const getLeaveTypeApi = id => getFactory(`${apiEndpoint}/${id}`);
export const removeLeaveTypeApi = id => delFactory(`${apiEndpoint}/${id}`);
export const updateLeaveTypeApi = leaveTypeObject =>
  putFactory(apiEndpoint, leaveTypeObject);
