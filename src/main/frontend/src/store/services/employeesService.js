import request from 'superagent';

export const getEmployeesApi = async () => {
  try {
    return await request
      .get('/users')
      .set('X-ID-TOKEN', sessionStorage.getItem('token'))
      .then(res => res.body);
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
