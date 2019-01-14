import { combineReducers } from 'redux';

import employee from './randomUserApiReducer';
import authorization from './getTokenReducer';
import teams from './teamsReducer';
import user from './userReducer';
import calendar from './calendarReducer';
import publicHolidays from './publicHolidayReducer';

export default combineReducers({
  authorization,
  employee,
  teams,
  user,
  calendar,
  publicHolidays
});
