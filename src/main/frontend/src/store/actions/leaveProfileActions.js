import { createAction } from 'redux-actions';

export const REQUEST_LEAVE_PROFILE = 'REQUEST_LEAVE_PROFILE';
export const RECEIVE_LEAVE_PROFILE = 'RECEIVE_LEAVE_PROFILE';
export const RECEIVE_LEAVE_PROFILE_ERROR = 'RECEIVE_LEAVE_PROFILE_ERROR';

export const requestLeaveProfile = createAction(REQUEST_LEAVE_PROFILE);
export const receiveLeaveProfile = createAction(RECEIVE_LEAVE_PROFILE);
export const errorReceivingLeaveProfile = createAction(
  RECEIVE_LEAVE_PROFILE_ERROR
);
