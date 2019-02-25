import request from 'superagent';
import { getLink } from '../getLink';

export const getEmployeesApi = () => {
  return request
    .get(getLink('/users'))
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const searchEmployeesApi = async user => {
  return await request
    .get(getLink('/users/search'))
    .query({ searchQuery: user, page: 0, size: 5, userStatusType: 'ACTIVE' })
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
