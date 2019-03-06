import { createAction } from 'redux-actions';

export const CREATE_LEAVE_REQUEST = 'CREATE_LEAVE_REQUEST';
export const CREATE_LEAVE_REQUEST_SUCCESSFUL =
  'CREATE_LEAVE_REQUEST_SUCCESSFUL';
export const CREATE_LEAVE_REQUEST_ERROR = 'CREATE_LEAVE_REQUEST_ERROR';

export const createLeaveRequest = createAction(CREATE_LEAVE_REQUEST);
export const createLeaveRequestSuccessful = createAction(
  CREATE_LEAVE_REQUEST_SUCCESSFUL
);
export const createLeaveRequestError = createAction(CREATE_LEAVE_REQUEST_ERROR);
