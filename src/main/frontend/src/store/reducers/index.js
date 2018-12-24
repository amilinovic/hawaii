import { combineReducers } from 'redux';

import employee from './randomUserApiReducer';
import authorization from './getTokenReducer';
import teams from './teamsReducer';
import user from './userReducer';
import calendar from './calendarReducer';

export default combineReducers({
  authorization,
  employee,
  teams,
  user,
  calendar
});
