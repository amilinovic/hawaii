import { combineReducers } from 'redux';

import authorization from './getTokenReducer';
import teams from './teamsReducer';
import user from './userReducer';
import calendar from './calendarReducer';
import publicHolidays from './publicHolidayReducer';
import personalDays from './personalDaysReducer';

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
  personalDays,
  request,
  leaveTypes
});
