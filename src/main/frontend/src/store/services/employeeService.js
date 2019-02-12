import request from 'superagent';

export const getEmployeeApi = async id => {
  return await request
    .get(`/users/${id}`)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const updateEmployeeApi = async employeeObject => {
  return await request
    .put('/users')
    .set(employeeObject)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
