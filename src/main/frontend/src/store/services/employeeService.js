import request from 'superagent';

export const getEmployeeApi = id => {
  return request
    .get(`/users/${id}`)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const removeEmployeeApi = id => {
  return request
    .del(`/users/${id}`)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
