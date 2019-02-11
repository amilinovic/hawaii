import request from 'superagent';

export const getTeamApi = async id => {
  return await request
    .get(`/teams/${id}`)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const createTeamApi = async teamObject => {
  return await request
    .post('/teams')
    .send(teamObject)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};

export const removeTeamApi = async id => {
  return await request
    .del(`/teams/${id}`)
    .set('X-ID-TOKEN', sessionStorage.getItem('token'))
    .then(res => res.body);
};
