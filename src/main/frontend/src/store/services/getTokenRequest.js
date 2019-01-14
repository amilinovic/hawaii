import request from 'superagent';
import { getTokenFromSessionStorage } from './getTokenFromSessionStorage';

export const tokenRequest = async userObj => {
  try {
    const response = await request
      .get(`/users/me`)
      .set('X-ID-TOKEN', userObj.tokenId)
      .set('Accept', 'application/json');

    const [token, role, user] = [
      response.headers['x-auth-token'],
      response.headers.role,
      response.body
    ];

    sessionStorage.setItem('token', userObj.tokenId);
    sessionStorage.setItem('role', response.body.userRole);
    sessionStorage.setItem('user', JSON.stringify(response.body));
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
