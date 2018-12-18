import request from 'superagent';

export const getUserApi = async email => {
  console.log(email);
  try {
    return await request
      .get(`/users/${email}`)
      .set('X-ID-TOKEN', sessionStorage.getItem('token'))
      .then(res => res.body)
      .catch(err => {
        console.log(err);
      });
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
