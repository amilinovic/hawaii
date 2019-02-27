import { getFactory, searchFactory } from './request';

const apiEndpoint = '/users/search';

export const getEmployeesApi = getFactory('/users');
export const searchEmployeesApi = user =>
  searchFactory(apiEndpoint, {
    searchQuery: user,
    page: 0,
    size: 5,
    userStatusType: 'ACTIVE'
  });

// export const searchEmployeesApi = async user => {
//   return await request
//     .get(getLink('/users/search'))
//     .query({ searchQuery: user, page: 0, size: 5, userStatusType: 'ACTIVE' })
//     .set('X-ID-TOKEN', sessionStorage.getItem('token'))
//     .then(res => res.body);
// };
