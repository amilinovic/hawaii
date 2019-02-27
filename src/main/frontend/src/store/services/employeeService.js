import { delFactory, getFactory, postFactory, putFactory } from './request';

const apiEndpoint = '/users';

export const getEmployeeApi = id => getFactory(`${apiEndpoint}/${id}`);
export const removeEmployeeApi = id => delFactory(`${apiEndpoint}/${id}`);
export const createEmployeeApi = employeeObject =>
  postFactory(apiEndpoint, employeeObject);
export const updateEmployeeApi = employeeObject =>
  putFactory(apiEndpoint, employeeObject);
