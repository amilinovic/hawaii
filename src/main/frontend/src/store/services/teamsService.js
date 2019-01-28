import request from 'superagent';

export const getTeamsApi = async () => {
  try {
    return await request
      .get('/teams')
      .set('X-ID-TOKEN', sessionStorage.getItem('token'))
      .then(res => res.body);
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
