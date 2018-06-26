import request from 'superagent';

export const fetchData = async () => {
  try {
    const response = await request
      .get('https://randomuser.me/api/?gender=female')
      .then(res => {
        return res.body;
      })
      .catch(err => {
        console.log(err);
      });
    return response;
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
};
