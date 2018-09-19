import request from 'superagent';

export const tokenRequest = async accessToken => {
  try {
    const response = await request
      .get('/signin')
      .set('Authorization', accessToken)
      .set('Accept', 'application/json');

    const [token, role, user] = [
      response.headers['x-auth-token'],
      response.headers.role,
      response.body
    ];

    sessionStorage.setItem('token', token);
    sessionStorage.setItem('role', role);
    sessionStorage.setItem('user', user);
    return {
      token,
      role,
      user
    };
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
