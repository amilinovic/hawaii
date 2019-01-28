import { get } from './request';

export const getUserApi = () => {
  try {
    return get(`/users/me`);
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
