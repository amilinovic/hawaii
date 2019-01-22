import { combineReducers } from 'redux';

import authorization from './getTokenReducer';
import teams from './teamsReducer';
import user from './userReducer';
import employees from './employeesReducer';

export default combineReducers({
  authorization,
  teams,
  user,
  employees
});
