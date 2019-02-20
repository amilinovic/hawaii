import request from 'superagent';
import { getTokenFromSessionStorage } from './getTokenFromSessionStorage';

const get = (url, user = getTokenFromSessionStorage().token) => {
  return request
    .get(url)
    .set('X-ID-TOKEN', user)
    .then(res => res.body);
};

const post = (url, data, user = getTokenFromSessionStorage().token) => {
  return request
    .post(url)
    .send(data)
    .set('X-ID-TOKEN', user)
    .then(res => res.body);
};

const put = (url, data, user = getTokenFromSessionStorage().token) => {
  return request
    .put(url)
    .send(data)
    .set('X-ID-TOKEN', user)
    .then(res => res.body);
};

const del = (url, user = getTokenFromSessionStorage().token) => {
  return request
    .del(url)
    .set('X-ID-TOKEN', user)
    .then(res => res.body);
};

export const getFactory = url => async () => await get(url);
export const delFactory = url => async () => await del(url);
export const postFactory = (url, data) => async () => await post(url, data);
export const putFactory = (url, data) => async () => await put(url, data);
