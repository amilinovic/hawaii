import { combineReducers } from 'redux';

import authorization from './getTokenReducer';
import teams from './teamsReducer';
import user from './userReducer';
import calendar from './calendarReducer';
import publicHolidays from './publicHolidayReducer';
import myPersonalDays from './myPersonalDaysReducer';
import receiveUsers from './teamCalendarReducer';

import employees from './employeesReducer';
import request from './requestReducer';
import leaveTypes from './leaveTypesReducer';

export default combineReducers({
  authorization,
  teams,
  user,
  calendar,
  publicHolidays,
  employees,
  myPersonalDays,
  request,
  leaveTypes,
  receiveUsers
});
