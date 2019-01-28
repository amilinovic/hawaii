import request from 'superagent';

export const getMyPersonalDaysApi = async () => {
  try {
    return await request
      .get('/users/myDays')
      .set('X-ID-TOKEN', sessionStorage.getItem('token'))
      .then(res => res.body);
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
