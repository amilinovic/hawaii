import { reducer as toastr } from 'react-redux-toastr';
import { combineReducers } from 'redux';
import employee from './employeeReducer';
import employees from './employeesReducer';
import employeesSearch from './employeesSearchReducer';
import authorization from './getTokenReducer';
import leaveProfile from './leaveProfileReducer';
import leaveProfiles from './leaveProfilesReducer';
import leaveTypes from './leaveTypesReducer';
import personalDays from './personalDaysReducer';
import publicHolidays from './publicHolidaysReducer';
import team from './teamReducer';
import teams from './teamsReducer';
import user from './userReducer';

export default combineReducers({
  authorization,
  teams,
  team,
  user,
  employees,
  employeesSearch,
  employee,
  personalDays,
  publicHolidays,
  leaveTypes,
  leaveProfiles,
  leaveProfile,
  toastr
});
