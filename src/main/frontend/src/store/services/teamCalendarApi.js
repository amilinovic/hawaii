import request from 'superagent';

export const getUsersApi = async teamId => {
  try {
    var res = teamId
      ? await request
          .get('/users/allUsers')
          .set('X-ID-TOKEN', sessionStorage.getItem('token'))
          .then(res => res.body)
          .catch(err => {
            console.log(err);
          })
      : await request
          .get('/users/allUsers')
          .set('X-ID-TOKEN', sessionStorage.getItem('token'))
          .then(res => res.body)
          .catch(err => {
            console.log(err);
          });
    return res;
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
