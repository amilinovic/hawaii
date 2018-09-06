import { createAction } from 'redux-actions';

export const REQUEST_EMPLOYEES = 'REQUEST_EMPLOYEES';
export const REQUEST_EMPLOYEES_SUCCEEDED = 'REQUEST_EMPLOYEES_SUCCEEDED';
export const REQUEST_EMPLOYEES_FAILED = 'RECEIVE_TEAMS_FAILED';

export const requestEmployees = createAction(REQUEST_EMPLOYEES);
export const requestEmployeesSucceeded = createAction(
  REQUEST_EMPLOYEES_SUCCEEDED
);
export const requestEmployeesFailed = createAction(REQUEST_EMPLOYEES_FAILED);

// console.log(requestEmployeesSucceeded)
