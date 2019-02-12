import request from 'superagent';

export const createEmployeeApi = async employeeObject => {
  console.log(employeeObject);
  return await request
    .post(`/users`)
    .send(employeeObject)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
