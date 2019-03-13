import { createAction } from 'redux-actions';

export const REQUEST_PUBLIC_HOLIDAY = 'REQUEST_PUBLIC_HOLIDAY';
export const RECEIVE_PUBLIC_HOLIDAY = 'RECEIVE_PUBLIC_HOLIDAY';
export const RECEIVE_PUBLIC_HOLIDAY_ERROR = 'RECEIVE_PUBLIC_HOLIDAY_ERROR';

export const requestPublicHoliday = createAction(REQUEST_PUBLIC_HOLIDAY);
export const receivePublicHoliday = createAction(RECEIVE_PUBLIC_HOLIDAY);
export const errorReceivingPublicHoliday = createAction(
  RECEIVE_PUBLIC_HOLIDAY_ERROR
);

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

export const CREATE_TEAM = 'CREATE_TEAM';
export const CREATE_TEAM_SUCCESSFUL = 'CREATE_TEAM_SUCCESSFUL';
export const CREATE_TEAM_ERROR = 'CREATE_TEAM_ERROR';

export const createTeam = createAction(CREATE_TEAM);
export const createTeamSuccessful = createAction(CREATE_TEAM_SUCCESSFUL);
export const createTeamError = createAction(CREATE_TEAM_ERROR);
