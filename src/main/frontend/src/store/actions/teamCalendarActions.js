import { createAction } from 'redux-actions';

export const INIT_TEAM_CALENDAR = 'INIT_TEAM_CALENDAR';
export const INCREMENT_MONTH = 'INCREMENT_MONTH';
export const DECREMENT_MONTH = 'DECREMENT_MONTH';
export const SELECT_MONTH = 'SELECT_MONTH';
export const SELECT_TEAM = 'SELECT_TEAM';
export const SELECT_VIEW_MODE = 'SELECT_VIEW_MODE';
export const REQUEST_USERS = 'REQUEST_USERS';
export const RECEIVE_USERS = 'RECEIVE_USERS';

export const initTeamCalendar = createAction(INIT_TEAM_CALENDAR);
export const incrementMonth = createAction(INCREMENT_MONTH);
export const decrementMonth = createAction(DECREMENT_MONTH);
export const selectMonth = createAction(SELECT_MONTH);
export const selectTeam = createAction(SELECT_TEAM);
export const selectViewMode = createAction(SELECT_VIEW_MODE);
export const requestUsers = createAction(REQUEST_USERS);
export const receiveUsers = createAction(RECEIVE_USERS);
