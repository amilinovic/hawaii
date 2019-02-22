import request from 'superagent';
import { getLink } from '../getLink';

export const getUserApi = () => {
  return request
    .get(getLink('/users/me'))
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
