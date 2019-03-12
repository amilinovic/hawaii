import { getFactory } from './request';

export const getLeaveProfileApi = id => getFactory(`/leaveprofiles/${id}`);
