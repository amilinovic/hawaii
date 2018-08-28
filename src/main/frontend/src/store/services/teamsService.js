import request from 'superagent';

export const getTeamsApi = () => {
  try {
    const response = request
      .get('/teams')
      .then(res => res.body)
      .catch(err => {
        console.log(err);
      });
    return response;
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
