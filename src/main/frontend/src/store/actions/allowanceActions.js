import { createAction } from 'redux-actions';

export const REQUEST_ALLOWANCE = 'REQUEST_ALLOWANCE';
export const RECEIVE_ALLOWANCE = 'RECEIVE_ALLOWANCE';
export const RECEIVE_ALLOWANCE_ERROR = 'RECEIVE_ALLOWANCE_ERROR';

export const requestAllowance = createAction(REQUEST_ALLOWANCE);
export const receiveAllowance = createAction(RECEIVE_ALLOWANCE);
export const errorReceivingAllowance = createAction(RECEIVE_ALLOWANCE_ERROR);
