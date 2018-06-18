import { FETCH_USERS } from './types';

export const fetchUsers = () => dispatch => {
  fetch('http://randomuser.me/api/')
    .then(res => res.json())
    .then(users =>
      dispatch({
        type: FETCH_USERS,
        payload: users.results
      })
    );
};
