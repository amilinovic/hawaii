import { get } from './request';

export const getEmployeesApi = async () => {
  try {
    return await get('/users');
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
