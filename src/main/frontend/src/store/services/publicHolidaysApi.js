import { get } from './request';

export const getPublicHolidaysApi = async () => {
  try {
    return await get('/publicholidays?deleted=false');
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
