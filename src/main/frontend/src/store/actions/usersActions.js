import { createAction } from 'redux-actions';

export const REQUEST_USERS = 'REQUEST_USERS';
export const RECEIVE_USERS = 'RECEIVE_USERS';
export const RECEIVE_USERS_ERROR = 'RECEIVE_USERS_ERROR';

export const requestUsers = createAction(REQUEST_USERS);
export const receiveUsers = createAction(RECEIVE_USERS);
export const errorReceivingUsers = createAction(RECEIVE_USERS_ERROR);
