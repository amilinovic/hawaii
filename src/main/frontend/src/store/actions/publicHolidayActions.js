import { createAction } from 'redux-actions';

export const REQUEST_PUBLIC_HOLIDAYS = 'REQUEST_PUBLIC_HOLIDAYS';
export const RECEIVE_PUBLIC_HOLIDAYS = 'RECEIVE_PUBLIC_HOLIDAYS';
export const RECEIVE_PUBLIC_HOLIDAYS_ERROR = 'RECEIVE_PUBLIC_HOLIDAYS_ERROR';

export const requestPublicHolidays = createAction(REQUEST_PUBLIC_HOLIDAYS);
export const receivePublicHolidays = createAction(RECEIVE_PUBLIC_HOLIDAYS);
export const errorReceivingPublicHolidays = createAction(
  RECEIVE_PUBLIC_HOLIDAYS_ERROR
);
