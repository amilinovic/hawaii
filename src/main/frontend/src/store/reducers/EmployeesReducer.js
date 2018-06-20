import { RECEIVE_API_DATA } from '../actions/EmployeesActions';

export default (state = { fetching: '' }, { type, employeeInformation }) => {
  switch (type) {
    case RECEIVE_API_DATA:
      return { fetching: 'done', employeeInformation };
    default:
      return state;
  }
};
