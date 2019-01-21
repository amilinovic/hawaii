import { createAction } from 'redux-actions';

export const REQUEST_MY_PERSONAL_DAYS = 'REQUEST_MY_PERSONAL_DAYS';
export const RECEIVE_MY_PERSONAL_DAYS = 'RECEIVE_MY_PERSONAL_DAYS';
export const RECEIVE_PUBLIC_HOLIDAYS_ERROR = 'RECEIVE_PUBLIC_HOLIDAYS_ERROR';

export const requestMyPersonalDays = createAction(REQUEST_MY_PERSONAL_DAYS);
export const receiveMyPersonalDays = createAction(RECEIVE_MY_PERSONAL_DAYS);
export const errorReceivingMyPersonalDays = createAction(
  RECEIVE_PUBLIC_HOLIDAYS_ERROR
);
