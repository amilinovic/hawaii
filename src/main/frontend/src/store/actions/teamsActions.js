import { createAction } from 'redux-actions';

export const REQUEST_TEAMS = 'REQUEST_TEAMS';
export const RECEIVE_TEAMS = 'RECEIVE_TEAMS';
export const RECEIVE_TEAMS_ERROR = 'RECEIVE_TEAMS_ERROR';

export const requestTeams = createAction(REQUEST_TEAMS);
export const receiveTeams = createAction(RECEIVE_TEAMS);
export const errorReceivingTeams = createAction(RECEIVE_TEAMS_ERROR);
