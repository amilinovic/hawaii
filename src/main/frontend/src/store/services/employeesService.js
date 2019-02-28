import { getFactory, searchFactory } from './request';

const apiEndpoint = '/users';

export const getEmployeesApi = getFactory(apiEndpoint);
export const searchEmployeesApi = user =>
  searchFactory(`${apiEndpoint}/search`, {
    searchQuery: user,
    page: 0,
    size: 5,
    userStatusType: 'ACTIVE'
  });
