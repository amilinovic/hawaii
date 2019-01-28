import { get } from './request';

export const getTeamsApi = async () => {
  try {
    return await get('/teams');
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
