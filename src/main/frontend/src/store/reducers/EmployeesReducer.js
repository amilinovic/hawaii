import { RECEIVE_API_DATA } from '../actions/EmployeesActions';

const actionHandlers = {
  [RECEIVE_API_DATA](employeeInformation) {
    return { fetching: 'done', employeeInformation };
  }
};

export default (state = { fetching: '' }, { type, employeeInformation }) => {
  const actionHandler = actionHandlers[type];
  if (actionHandler) {
    return actionHandler(employeeInformation);
  }
  return state;
};
