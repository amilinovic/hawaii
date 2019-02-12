import request from 'superagent';

export const getEmployeeApi = async id => {
  return await request
    .get(`/users/${id}`)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const removeEmployeeApi = async id => {
  return await request
    .del(`/users/${id}`)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
