export const getTokenFromSessionStorage = () => {
  const token = sessionStorage.getItem('token');
  const role = sessionStorage.getItem('role');
  return token != null && role != null
    ? {
        token,
        role
      }
    : null;
};
