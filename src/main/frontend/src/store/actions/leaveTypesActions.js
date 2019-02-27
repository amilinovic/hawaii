import { createAction } from 'redux-actions';

export const REQUEST_LEAVE_TYPES = 'REQUEST_LEAVE_TYPES';
export const RECEIVE_LEAVE_TYPES = 'RECEIVE_LEAVE_TYPES';
export const RECEIVE_LEAVE_TYPES_ERROR = 'RECEIVE_LEAVE_TYPES_ERROR';

export const requestLeaveTypes = createAction(REQUEST_LEAVE_TYPES);
export const receiveLeaveTypes = createAction(RECEIVE_LEAVE_TYPES);
export const errorReceivingLeaveTypes = createAction(RECEIVE_LEAVE_TYPES_ERROR);
