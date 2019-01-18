import { createAction } from 'redux-actions';

export const REQUEST_USER = 'REQUEST_USER';
export const RECEIVE_USER = 'RECEIVE_USER';
export const RECEIVE_USER_ERROR = 'RECEIVE_USER_ERROR';

export const requestUser = createAction(REQUEST_USER);
export const receiveUser = createAction(RECEIVE_USER);
export const errorReceivingUser = createAction(RECEIVE_USER_ERROR);
