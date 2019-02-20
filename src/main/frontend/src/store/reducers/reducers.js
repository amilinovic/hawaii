import { combineReducers } from 'redux';
import employee from './employeeReducer';
import employees from './employeesReducer';
import authorization from './getTokenReducer';
import leaveTypes from './leaveTypesReducer';
import personalDays from './personalDaysReducer';
import publicHoliday from './publicHolidayReducer';
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
  personalDays,
  publicHoliday,
  leaveTypes
});
