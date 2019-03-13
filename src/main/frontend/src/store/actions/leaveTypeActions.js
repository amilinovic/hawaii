import { createAction } from 'redux-actions';

export const REQUEST_LEAVE_TYPE = 'REQUEST_LEAVE_TYPE';
export const RECEIVE_LEAVE_TYPE = 'RECEIVE_LEAVE_TYPE';
export const RECEIVE_LEAVE_TYPE_ERROR = 'RECEIVE_LEAVE_TYPE_ERROR';

export const requestLeaveType = createAction(REQUEST_LEAVE_TYPE);
export const receiveLeaveType = createAction(RECEIVE_LEAVE_TYPE);
export const errorReceivingLeaveType = createAction(RECEIVE_LEAVE_TYPE_ERROR);

export const REMOVE_LEAVE_TYPE = 'REMOVE_LEAVE_TYPE';
export const REMOVE_LEAVE_TYPE_SUCCESS = 'REMOVE_LEAVE_TYPE_SUCCESS';
export const REMOVE_LEAVE_TYPE_ERROR = 'REMOVE_LEAVE_TYPE_ERROR';

export const removeLeaveType = createAction(REMOVE_LEAVE_TYPE);
export const removeLeaveTypeSuccess = createAction(REMOVE_LEAVE_TYPE_SUCCESS);
export const errorRemovingLeaveType = createAction(REMOVE_LEAVE_TYPE_ERROR);

export const UPDATE_LEAVE_TYPE = 'UPDATE_LEAVE_TYPE';
export const UPDATE_LEAVE_TYPE_SUCCESSFUL = 'UPDATE_LEAVE_TYPE_SUCCESSFUL';
export const UPDATE_LEAVE_TYPE_ERROR = 'UPDATE_LEAVE_TYPE_ERROR';

export const updateLeaveType = createAction(UPDATE_LEAVE_TYPE);
export const updateLeaveTypeSuccessful = createAction(
  UPDATE_LEAVE_TYPE_SUCCESSFUL
);
export const updateLeaveTypeError = createAction(UPDATE_LEAVE_TYPE_ERROR);

export const CREATE_LEAVE_TYPE = 'CREATE_LEAVE_TYPE';
export const CREATE_LEAVE_TYPE_SUCCESSFUL = 'CREATE_LEAVE_TYPE_SUCCESSFUL';
export const CREATE_LEAVE_TYPE_ERROR = 'CREATE_LEAVE_TYPE_ERROR';

export const createLeaveType = createAction(CREATE_LEAVE_TYPE);
export const createLeaveTypeSuccessful = createAction(
  CREATE_LEAVE_TYPE_SUCCESSFUL
);
export const createLeaveTypeError = createAction(CREATE_LEAVE_TYPE_ERROR);
