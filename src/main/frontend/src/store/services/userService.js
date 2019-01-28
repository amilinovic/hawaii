import request from 'superagent';

export const getUserApi = () => {
  try {
    return request
      .get(`/users/me`)
      .set('X-ID-TOKEN', sessionStorage.getItem('token'))
      .then(res => res.body);
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
