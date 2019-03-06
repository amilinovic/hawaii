import { createAction } from 'redux-actions';

export const SEARCH_EMPLOYEES = 'SEARCH_EMPLOYEES';
export const SEARCH_EMPLOYEES_SUCCESS = 'SEARCH_EMPLOYEES_SUCCESS';
export const SEARCH_EMPLOYEES_ERROR = 'SEARCH_EMPLOYEES_ERROR';

export const searchEmployees = createAction(SEARCH_EMPLOYEES);
export const searchEmployeesSuccess = createAction(SEARCH_EMPLOYEES_SUCCESS);
export const errorSearchingEmployees = createAction(SEARCH_EMPLOYEES_ERROR);

export const CLEAR_EMPLOYEES = 'CLEAR_EMPLOYEES';

export const clearEmployees = createAction(CLEAR_EMPLOYEES);
