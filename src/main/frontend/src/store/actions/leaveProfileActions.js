import { createAction } from 'redux-actions';

export const REQUEST_LEAVE_PROFILE = 'REQUEST_LEAVE_PROFILE';
export const RECEIVE_LEAVE_PROFILE = 'RECEIVE_LEAVE_PROFILE';
export const RECEIVE_LEAVE_PROFILE_ERROR = 'RECEIVE_LEAVE_PROFILE_ERROR';

export const requestLeaveProfile = createAction(REQUEST_LEAVE_PROFILE);
export const receiveLeaveProfile = createAction(RECEIVE_LEAVE_PROFILE);
export const errorReceivingLeaveProfile = createAction(
  RECEIVE_LEAVE_PROFILE_ERROR
);

export const REMOVE_LEAVE_PROFILE = 'REMOVE_LEAVE_PROFILE';
export const REMOVE_LEAVE_PROFILE_SUCCESS = 'REMOVE_LEAVE_PROFILE_SUCCESS';
export const REMOVE_LEAVE_PROFILE_ERROR = 'REMOVE_LEAVE_PROFILE_ERROR';

export const removeLeaveProfile = createAction(REMOVE_LEAVE_PROFILE);
export const removeLeaveProfileSuccess = createAction(
  REMOVE_LEAVE_PROFILE_SUCCESS
);
export const errorRemovingLeaveProfile = createAction(
  REMOVE_LEAVE_PROFILE_ERROR
);

export const CREATE_LEAVE_PROFILE = 'CREATE_LEAVE_PROFILE';
export const CREATE_LEAVE_PROFILE_SUCCESSFUL =
  'CREATE_LEAVE_PROFILE_SUCCESSFUL';
export const CREATE_LEAVE_PROFILE_ERROR = 'CREATE_LEAVE_PROFILE_ERROR';

export const createLeaveProfile = createAction(CREATE_LEAVE_PROFILE);
export const createLeaveProfileSuccessful = createAction(
  CREATE_LEAVE_PROFILE_SUCCESSFUL
);
export const createLeaveProfileError = createAction(CREATE_LEAVE_PROFILE_ERROR);

export const UPDATE_LEAVE_PROFILE = 'UPDATE_LEAVE_PROFILE';
export const UPDATE_LEAVE_PROFILE_SUCCESSFUL =
  'UPDATE_LEAVE_PROFILE_SUCCESSFUL';
export const UPDATE_LEAVE_PROFILE_ERROR = 'UPDATE_LEAVE_PROFILE_ERROR';

export const updateLeaveProfile = createAction(UPDATE_LEAVE_PROFILE);
export const updateLeaveProfileSuccessful = createAction(
  UPDATE_LEAVE_PROFILE_SUCCESSFUL
);
export const updateLeaveProfileError = createAction(UPDATE_LEAVE_PROFILE_ERROR);
