import request from 'superagent';
import { getLink } from '../getLink';

export const getEmployeesApi = async () => {
  try {
    return await request
      .get(getLink('/users'))
      .set('X-ID-TOKEN', sessionStorage.getItem('token'))
      .then(res => res.body);
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
