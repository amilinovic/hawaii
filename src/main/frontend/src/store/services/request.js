import request from 'superagent';
import { getTokenFromSessionStorage } from './getTokenFromSessionStorage';

const get = (url, user = getTokenFromSessionStorage().token) => {
  return request
    .get(url)
    .set('X-ID-TOKEN', user)
    .then(res => res.body);
};

export const getFactory = url => async () => await get(url);
