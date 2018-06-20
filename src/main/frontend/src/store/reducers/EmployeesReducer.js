import { RECEIVE_API_DATA } from '../actions/EmployeesActions';

export default (state = { fetching: '' }, { type, data }) => {
  switch (type) {
    case RECEIVE_API_DATA:
      return { fetching: 'done', data };
    default:
      return state;
  }
};
