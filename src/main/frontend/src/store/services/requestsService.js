import { postFactory } from './request';

export const requestApi = requestObject =>
  postFactory('/requests', requestObject);
