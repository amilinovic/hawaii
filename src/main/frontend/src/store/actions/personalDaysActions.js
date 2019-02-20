import { createAction } from 'redux-actions';

export const REQUEST_PERSONAL_DAYS = 'REQUEST_PERSONAL_DAYS';
export const RECEIVE_PERSONAL_DAYS = 'RECEIVE_MY_PERSONAL_DAYS';
export const RECEIVE_PERSONAL_DAYS_ERROR = 'RECEIVE_PERSONAL_DAYS_ERROR';

export const requestPersonalDays = createAction(REQUEST_PERSONAL_DAYS);
export const receivePersonalDays = createAction(RECEIVE_PERSONAL_DAYS);
export const errorReceivingPersonalDays = createAction(
  RECEIVE_PERSONAL_DAYS_ERROR
);
