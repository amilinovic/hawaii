import { createAction } from 'redux-actions';

export const REQUEST_TEAM = 'REQUEST_TEAM';
export const RECEIVE_TEAM = 'RECEIVE_TEAM';
export const RECEIVE_TEAM_ERROR = 'RECEIVE_TEAM_ERROR';

export const requestTeam = createAction(REQUEST_TEAM);
export const receiveTeam = createAction(RECEIVE_TEAM);
export const errorReceivingTeam = createAction(RECEIVE_TEAM_ERROR);

export const REMOVE_TEAM = 'REMOVE_TEAM';
export const REMOVE_TEAM_SUCCESS = 'REMOVE_TEAM_SUCCESS';
export const REMOVE_TEAM_ERROR = 'REMOVE_TEAM_ERROR';

export const removeTeam = createAction(REMOVE_TEAM);
export const removeTeamSuccess = createAction(REMOVE_TEAM_SUCCESS);
export const errorRemovingTeam = createAction(REMOVE_TEAM_ERROR);

export const UPDATE_TEAM = 'UPDATE_TEAM';
export const UPDATE_TEAM_SUCCESSFUL = 'UPDATE_TEAM_SUCCESSFUL';
export const UPDATE_TEAM_ERROR = 'UPDATE_TEAM_ERROR';

export const updateTeam = createAction(UPDATE_TEAM);
export const updateTeamSuccessful = createAction(UPDATE_TEAM_SUCCESSFUL);
export const updateTeamError = createAction(UPDATE_TEAM_ERROR);
