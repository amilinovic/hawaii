import { createAction } from 'redux-actions';

export const REQUEST_YEARS = 'REQUEST_YEARS';
export const RECEIVE_YEARS = 'RECEIVE_YEARS';
export const RECEIVE_YEARS_ERROR = 'RECEIVE_YEARS_ERROR';

export const requestYears = createAction(REQUEST_YEARS);
export const receiveYears = createAction(RECEIVE_YEARS);
export const errorReceivingYears = createAction(RECEIVE_YEARS_ERROR);
