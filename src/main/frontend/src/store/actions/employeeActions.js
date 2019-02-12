import { createAction } from 'redux-actions';

export const REQUEST_EMPLOYEE = 'REQUEST_EMPLOYEE';
export const RECEIVE_EMPLOYEE = 'RECEIVE_EMPLOYEE';
export const RECEIVE_EMPLOYEE_ERROR = 'RECEIVE_EMPLOYEE_ERROR';

export const requestEmployee = createAction(REQUEST_EMPLOYEE);
export const receiveEmployee = createAction(RECEIVE_EMPLOYEE);
export const errorReceivingEmployee = createAction(RECEIVE_EMPLOYEE_ERROR);

export const REMOVE_EMPLOYEE = 'REMOVE_EMPLOYEE';
export const REMOVE_EMPLOYEE_SUCCESS = 'REMOVE_EMPLOYEE_SUCCESS';
export const REMOVE_EMPLOYEE_ERROR = 'REMOVE_EMPLOYEE_ERROR';

export const removeEmployee = createAction(REMOVE_EMPLOYEE);
export const removeEmployeeSuccess = createAction(REMOVE_EMPLOYEE_SUCCESS);
export const errorRemovingEmployee = createAction(REMOVE_EMPLOYEE_ERROR);
