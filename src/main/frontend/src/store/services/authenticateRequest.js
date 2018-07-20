import request from 'superagent';

export const authenticateRequest = async () => {
  try {
    const response = await request.get('/authentication').then(() => {
      return true;
    });
    return response;
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
