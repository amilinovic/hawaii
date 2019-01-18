import { combineReducers } from 'redux';

import authorization from './getTokenReducer';
import teams from './teamsReducer';
import user from './userReducer';
import calendar from './calendarReducer';
import publicHolidays from './publicHolidayReducer';
import myPersonalDays from './myPersonalDaysReducer';
import employees from './employeesReducer';

export default combineReducers({
  authorization,
  teams,
  user,
  calendar,
  publicHolidays,
  employees,
  myPersonalDays
});
