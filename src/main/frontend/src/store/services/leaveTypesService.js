import { get } from './request';

export const getLeaveTypesService = async () => {
  try {
    return await get('/leavetypes');
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
