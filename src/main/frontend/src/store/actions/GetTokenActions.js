import { createAction } from 'redux-actions';

export const REQUEST_TOKEN = 'REQUEST_TOKEN';
export const RECEIVE_TOKEN = 'RECEIVE_TOKEN';
export const RECEIVE_TOKEN_ERROR = 'RECEIVE_TOKEN_ERROR';

export const requestToken = createAction(REQUEST_TOKEN);
export const receiveToken = createAction(RECEIVE_TOKEN);
export const receiveTokenError = createAction(RECEIVE_TOKEN_ERROR);
