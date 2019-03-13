import { delFactory, getFactory, postFactory, putFactory } from './request';

const apiEndpoint = '/years';

export const createYearApi = yearObject => postFactory(apiEndpoint, yearObject);
export const getYearApi = id => getFactory(`${apiEndpoint}/${id}`);
export const removeYearApi = id => delFactory(`${apiEndpoint}/${id}`);
export const updateYearApi = yearObject => putFactory(apiEndpoint, yearObject);
