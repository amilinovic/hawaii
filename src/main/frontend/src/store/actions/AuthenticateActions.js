import { createAction } from 'redux-actions';

export const REQUEST_AUTHENTICATION = 'REQUEST_AUTHENTICATION';
export const RECEIVE_AUTHENTICATION = 'RECEIVE_AUTHENTICATION';
export const RECEIVE_AUTHENTICATION_ERROR = 'RECEIVE_AUTHENTICATION_ERROR';

export const requestAuthentication = createAction(REQUEST_AUTHENTICATION);
export const receiveAuthentication = createAction(RECEIVE_AUTHENTICATION);
export const receiveAuthenticationError = createAction(
  RECEIVE_AUTHENTICATION_ERROR
);
