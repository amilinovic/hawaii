import request from 'superagent';

export const getPublicHolidaysApi = async () => {
  try {
    return await request
      .get('/publicholidays?deleted=false')
      .set('X-ID-TOKEN', sessionStorage.getItem('token'))
      .then(res => res.body);
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
