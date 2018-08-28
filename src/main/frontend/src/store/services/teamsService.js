import request from 'superagent';

export const getTeamsApi = async () => {
  try {
    return await request
      .get('/teams')
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
