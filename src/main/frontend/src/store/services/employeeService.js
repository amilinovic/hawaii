import request from 'superagent';

export const createEmployeeApi = async employeeObject => {
  return await request
    .post(`/users`)
    .send(employeeObject)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const getEmployeeApi = async id => {
  return await request
    .get(`/users/${id}`)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const updateEmployeeApi = async employeeObject => {
  return await request
    .put('/users')
    .send(employeeObject)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const removeEmployeeApi = async id => {
  return await request
    .del(`/users/${id}`)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
