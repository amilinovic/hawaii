import request from 'superagent';
import { getTokenFromSessionStorage } from './getTokenFromSessionStorage';

export const get = (url, user = getTokenFromSessionStorage().token) => {
  return request
    .get(url)
    .set('X-ID-TOKEN', user)
    .then(res => res.body);
};
