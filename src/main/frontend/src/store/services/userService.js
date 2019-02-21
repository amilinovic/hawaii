import request from 'superagent';

export const getUserApi = () => {
  return request
    .get('/users/medasd')
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
