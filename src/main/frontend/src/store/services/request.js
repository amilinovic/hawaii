import request from 'superagent';
import { getLink } from '../getLink';
import { getTokenFromSessionStorage } from './getTokenFromSessionStorage';

const get = (url, data) => {
  return request
    .get(getLink(url))
    .query(data)
    .set('X-ID-TOKEN', getTokenFromSessionStorage().token)
    .then(res => res.body);
};

const post = (url, data) => {
  return request
    .post(getLink(url))
    .send(data)
    .set('X-ID-TOKEN', getTokenFromSessionStorage().token)
    .then(res => res.body);
};

const put = (url, data) => {
  return request
    .put(getLink(url))
    .send(data)
    .set('X-ID-TOKEN', getTokenFromSessionStorage().token)
    .then(res => res.body);
};

const del = url => {
  return request
    .del(getLink(url))
    .set('X-ID-TOKEN', getTokenFromSessionStorage().token)
    .then(res => res.body);
};

const search = (url, query) => {
  return request
    .get(getLink(url))
    .query(query)
    .set('X-ID-TOKEN', getTokenFromSessionStorage().token)
    .then(res => res.body);
};

export const getFactory = (url, data) => async () => await get(url, data);
export const delFactory = url => async () => await del(url);
export const postFactory = (url, data) => async () => await post(url, data);
export const putFactory = (url, data) => async () => await put(url, data);
export const searchFactory = (url, query) => async () =>
  await search(url, query);
