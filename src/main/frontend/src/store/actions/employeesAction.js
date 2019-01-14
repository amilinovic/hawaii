import { createAction } from 'redux-actions';

export const REQUEST_EMPLOYEES = 'REQUEST_EMPLOYEES';
export const RECEIVE_EMPLOYEES = 'RECEIVE_EMPLOYEES';
export const RECEIVE_EMPLOYEES_ERROR = 'RECEIVE_EMPLOYEES_ERROR';

export const requestEmployees = createAction(REQUEST_EMPLOYEES);
export const receiveEmployees = createAction(RECEIVE_EMPLOYEES);
export const errorReceivingEmployees = createAction(RECEIVE_EMPLOYEES_ERROR);
