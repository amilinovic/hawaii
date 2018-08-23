import { createAction } from 'redux-actions';

export const REQUEST_TOKEN_FROM_STORAGE = 'REQUEST_TOKEN_FROM_STORAGE';
export const RECEIVE_TOKEN_FROM_STORAGE_ERROR =
  'RECEIVE_TOKEN_FROM_STORAGE_ERROR';

export const requestTokenFromStorage = createAction(REQUEST_TOKEN_FROM_STORAGE);
export const receiveTokenFromStorageError = createAction(
  RECEIVE_TOKEN_FROM_STORAGE_ERROR
);
