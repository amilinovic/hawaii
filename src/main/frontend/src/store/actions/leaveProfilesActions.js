import { createAction } from 'redux-actions';

export const REQUEST_LEAVE_PROFILES = 'REQUEST_LEAVE_PROFILES';
export const RECEIVE_LEAVE_PROFILES = 'RECEIVE_LEAVE_PROFILES';
export const RECEIVE_LEAVE_PROFILES_ERROR = 'RECEIVE_LEAVE_PROFILES_ERROR';

export const requestLeaveProfiles = createAction(REQUEST_LEAVE_PROFILES);
export const receiveLeaveProfiles = createAction(RECEIVE_LEAVE_PROFILES);
export const errorReceivingLeaveProfiles = createAction(
  RECEIVE_LEAVE_PROFILES_ERROR
);
