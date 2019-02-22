import request from 'superagent';
import { getLink } from '../getLink';

export const getTeamsApi = async () => {
  try {
    return await request
      .get(getLink('/teams'))
      .set('X-ID-TOKEN', sessionStorage.getItem('token'))
      .then(res => res.body)
      .catch(err => {
        console.log(err);
      });
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
