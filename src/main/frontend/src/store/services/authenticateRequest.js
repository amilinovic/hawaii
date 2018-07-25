export const authenticateRequest = () => {
  try {
    const token = sessionStorage.getItem('token');
    const role = sessionStorage.getItem('role');
    if (token != null && role != null) {
      return {
        token: token,
        role: role,
        request: true
      };
    } else {
      return {
        request: false
      };
    }
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
  return false;
};
