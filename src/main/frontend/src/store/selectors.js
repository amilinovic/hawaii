import { receiveUsers } from './actions/teamCalendarActions';

export const getAuthorization = state => state.authorization;
export const getTeams = state => state.teams;
export const getUser = state => state.user;
export const getCalendar = state => state.calendar;
export const getPublicHolidays = state => state.publicHolidays;
export const getMyPersonalDays = state => state.myPersonalDays;
export const getEmployees = state => state.employees;
export const getRequest = state => state.request;
export const getLeaveTypes = state => state.leaveTypes;
export const getUsers = state => state.receiveUsers;
