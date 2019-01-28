import { get } from './request';

export const getMyPersonalDaysApi = async () => {
  try {
    return await get('/users/myDays');
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
