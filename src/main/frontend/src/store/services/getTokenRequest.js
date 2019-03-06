import request from 'superagent';
import { getLink } from '../getLink';

export const tokenRequest = async userObj => {
  const response = await request
    .get(getLink('/users/me'))
    .set('X-ID-TOKEN', userObj.tokenId)
    .set('Accept', 'application/json');

  const [token, role, user] = [
    response.headers['x-auth-token'],
    response.headers.role,
    response.body
  ];

  sessionStorage.setItem('token', userObj.tokenId);
  sessionStorage.setItem('role', response.body.userRole);
  sessionStorage.setItem('userEmail', response.body.email);

  return {
    token,
    role,
    user
  };
};
