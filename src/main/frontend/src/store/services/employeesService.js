import request from 'superagent';
import { getLink } from '../getLink';

export const getEmployeesApi = () => {
  return request
    .get(getLink('/users'))
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
