import request from 'superagent';

export const getUsersApi = () => {
  return request
    .get('/users')
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
