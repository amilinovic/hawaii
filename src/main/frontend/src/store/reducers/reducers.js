import { combineReducers } from 'redux';
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
  employees
});
