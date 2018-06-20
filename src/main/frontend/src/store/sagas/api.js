import request from 'superagent';

export const fetchData = async () => {
  try {
    const response = request
      .get('https://randomuser.me/api')
      .then(res => {
        const data = res.body;
        return data;
      })
      .catch(err => {
        console.log(err);
      });
    const data = await response;
    return data;
  } catch (e) {
    console.log(e);
  }
};
