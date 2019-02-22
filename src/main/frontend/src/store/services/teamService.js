import request from 'superagent';
import { getLink } from '../getLink';

export const getTeamApi = async id => {
  return await request
    .get(getLink(`/teams/${id}`))
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const updateTeamApi = async teamObject => {
  return await request
    .put(getLink('/teams'))
    .send(teamObject)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const removeTeamApi = async id => {
  return await request
    .del(getLink(`/teams/${id}`))
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
