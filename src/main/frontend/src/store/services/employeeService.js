import request from 'superagent';
import { getLink } from '../getLink';

export const createEmployeeApi = async employeeObject => {
  return await request
    .post(getLink('/users'))
    .send(employeeObject)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const getEmployeeApi = async id => {
  return await request
    .get(getLink(`/users/${id}`))
    .post(`/users`)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const updateEmployeeApi = async employeeObject => {
  return await request
    .put(getLink('/users'))
    .send(employeeObject)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const removeEmployeeApi = async id => {
  return await request
    .del(getLink(`/users/${id}`))
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
