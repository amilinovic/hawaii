import { createAction } from 'redux-actions';

export const REQUEST_EMPLOYEE = 'REQUEST_EMPLOYEE';
export const RECEIVE_EMPLOYEE = 'RECEIVE_EMPLOYEE';
export const RECEIVE_EMPLOYEE_ERROR = 'RECEIVE_EMPLOYEE_ERROR';

export const requestEmployee = createAction(REQUEST_EMPLOYEE);
export const receiveEmployee = createAction(RECEIVE_EMPLOYEE);
export const errorReceivingEmployee = createAction(RECEIVE_EMPLOYEE_ERROR);

export const UPDATE_EMPLOYEE = 'UPDATE_EMPLOYEE';
export const UPDATE_EMPLOYEE_SUCCESSFUL = 'UPDATE_EMPLOYEE_SUCCESSFUL';
export const UPDATE_EMPLOYEE_ERROR = 'UPDATE_EMPLOYEE_ERROR';

export const updateEmployee = createAction(UPDATE_EMPLOYEE);
export const updateEmployeeSuccessful = createAction(
  UPDATE_EMPLOYEE_SUCCESSFUL
);
export const updateEmployeeError = createAction(UPDATE_EMPLOYEE_ERROR);
