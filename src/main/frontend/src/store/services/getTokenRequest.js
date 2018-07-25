import request from 'superagent';

export const tokenRequest = async accessToken => {
  try {
    const response = await request
      .get('/signin')
      .set('Authorization', accessToken)
      .set('Accept', 'application/json');

    const [token, role] = [
      response.headers['x-auth-token'],
      response.headers.role
    ];

    sessionStorage.setItem('token', token);
    sessionStorage.setItem('role', role);
    return {
      token,
      role
    };
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
