import { createAction } from 'redux-actions';

export const REQUEST_TOKEN = 'REQUEST_TOKEN';
export const RECEIVE_TOKEN = 'RECEIVE_TOKEN';
export const RECEIVE_TOKEN_ERROR = 'RECEIVE_TOKEN_ERROR';

export const requestAuthorization = createAction(REQUEST_TOKEN);
export const receiveAuthorization = createAction(RECEIVE_TOKEN);
export const receiveAuthorizationError = createAction(RECEIVE_TOKEN_ERROR);
