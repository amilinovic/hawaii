import { createAction } from 'redux-actions';

export const CREATE_EMPLOYEE = 'CREATE_EMPLOYEE';
export const CREATE_EMPLOYEE_SUCCESS = 'CREATE_EMPLOYEE_SUCCESS';
export const CREATE_EMPLOYEE_ERROR = 'CREATE_EMPLOYEE_ERROR';

export const createEmployee = createAction(CREATE_EMPLOYEE);
export const createEmployeeSuccess = createAction(CREATE_EMPLOYEE_SUCCESS);
export const errorCreatingEmployee = createAction(CREATE_EMPLOYEE_ERROR);
