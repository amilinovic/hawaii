import { reducer as toastr } from 'react-redux-toastr';
import { combineReducers } from 'redux';
import employee from './employeeReducer';
import employees from './employeesReducer';
import authorization from './getTokenReducer';
import team from './teamReducer';
import teams from './teamsReducer';
import user from './userReducer';

export default combineReducers({
  authorization,
  teams,
  team,
  user,
  employees,
  employee,
  toastr
});
