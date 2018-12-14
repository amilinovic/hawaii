import request from 'superagent';

export const tokenRequest = async userObj => {
  try {
    const response = await request
      .get(`/users/${userObj.profileObj.email}`)
      .set('X-ID-TOKEN', userObj.tokenId)
      .set('Accept', 'application/json');

    const [token, role, user] = [
      response.headers['x-auth-token'],
      response.headers.role,
      response.body
    ];

    // TODO vrackovic: Investigate `user` stored in sessionStorage and store it if needed in future
    sessionStorage.setItem('token', userObj.tokenId);
    sessionStorage.setItem('role', response.body.userRole);
    // sessionStorage.setItem('user', user);
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
