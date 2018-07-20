import request from 'superagent';

export const authorizationRequest = async accessToken => {
  try {
    const response = await request
      .get('/signin')
      .set('Authorization', accessToken)
      .set('Accept', 'application/json');
    return response;
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
