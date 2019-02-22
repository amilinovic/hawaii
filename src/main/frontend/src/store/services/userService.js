import request from 'superagent';

export const getUserApi = () => {
  return request
    .get('/users/me')
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
