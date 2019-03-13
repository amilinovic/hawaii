import { reducer as toastr } from 'react-redux-toastr';
import { combineReducers } from 'redux';
import allowance from './allowanceReducer';
import employee from './employeeReducer';
import employees from './employeesReducer';
import employeesSearch from './employeesSearchReducer';
import authorization from './getTokenReducer';
import leaveProfile from './leaveProfileReducer';
import leaveProfiles from './leaveProfilesReducer';
import leaveType from './leaveTypeReducer';
import leaveTypes from './leaveTypesReducer';
import modal from './modalReducer';
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
  leaveType,
  leaveProfiles,
  leaveProfile,
  modal,
  allowance,
  toastr
});
