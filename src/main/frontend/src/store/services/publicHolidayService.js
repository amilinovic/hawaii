import { delFactory, getFactory, postFactory, putFactory } from './request';

const apiEndpoint = '/publicholidays';

export const createPublicHolidayApi = publicHolidayObject =>
  postFactory(apiEndpoint, publicHolidayObject);
export const getPublicHolidayApi = id => getFactory(`${apiEndpoint}/${id}`);
export const removePublicHolidayApi = id => delFactory(`${apiEndpoint}/${id}`);
export const updatePublicHolidayApi = publicHolidayObject =>
  putFactory(apiEndpoint, publicHolidayObject);
