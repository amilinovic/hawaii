import { postFactory } from './request';

export const createLeaveRequestApi = requestObject =>
  postFactory('/requests', requestObject);
