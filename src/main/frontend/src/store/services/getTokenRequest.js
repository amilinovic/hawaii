import { getFactory } from './request';

export const tokenRequest = async userObj => {
  try {
    const response = getFactory('/users/me', userObj.tokenId);
    // const response = await get(`/users/me`, userObj.tokenId);

    const [token, role, userImageUrl] = [
      userObj.tokenId,
      response.userRole,
      userObj.profileObj.imageUrl
    ];

    // TODO vrackovic: Investigate `user` stored in sessionStorage and store it if needed in future
    sessionStorage.setItem('token', userObj.tokenId);
    sessionStorage.setItem('role', response.userRole);
    sessionStorage.setItem('userImageUrl', userObj.profileObj.imageUrl);
    return {
      token,
      role,
      userImageUrl
    };
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
