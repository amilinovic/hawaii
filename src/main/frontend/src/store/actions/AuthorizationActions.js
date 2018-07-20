import { createAction } from 'redux-actions';

export const REQUEST_AUTHORIZATION = 'REQUEST_AUTHORIZATION';
export const RECEIVE_AUTHORIZATION = 'RECEIVE_AUTHORIZATION';
export const RECEIVE_AUTHORIZATION_ERROR = 'RECEIVE_AUTHORIZATION_ERROR';

export const requestAuthorization = createAction(REQUEST_AUTHORIZATION);
export const receiveAuthorization = createAction(RECEIVE_AUTHORIZATION);
export const receiveAuthorizationError = createAction(
  RECEIVE_AUTHORIZATION_ERROR
);
