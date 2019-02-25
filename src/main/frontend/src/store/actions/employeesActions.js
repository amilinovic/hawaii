import { createAction } from 'redux-actions';

export const REQUEST_EMPLOYEES = 'REQUEST_EMPLOYEES';
export const RECEIVE_EMPLOYEES = 'RECEIVE_EMPLOYEES';
export const RECEIVE_EMPLOYEES_ERROR = 'RECEIVE_EMPLOYEES_ERROR';

export const requestEmployees = createAction(REQUEST_EMPLOYEES);
export const receiveEmployees = createAction(RECEIVE_EMPLOYEES);
export const errorReceivingEmployees = createAction(RECEIVE_EMPLOYEES_ERROR);

export const SEARCH_EMPLOYEES = 'SEARCH_EMPLOYEES';
export const SEARCH_EMPLOYEES_SUCCESS = 'SEARCH_EMPLOYEES_SUCCESS';
export const SEARCH_EMPLOYEES_ERROR = 'SEARCH_EMPLOYEES_ERROR';

export const searchEmployees = createAction(SEARCH_EMPLOYEES);
export const searchEmployeesSuccess = createAction(SEARCH_EMPLOYEES_SUCCESS);
export const errorSearchingEmployees = createAction(SEARCH_EMPLOYEES_ERROR);

export const CLEAR_EMPLOYEES = 'CLEAR_EMPLOYEES';

export const clearEmployees = createAction(CLEAR_EMPLOYEES);
