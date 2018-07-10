import { createSelector } from 'reselect';

const getEmployeeState = state => state.employee.employeeInformation;
const getFetchingState = state => state.employee.fetching;

export const getEmployee = createSelector(
  getEmployeeState,
  employee => employee
);

export const getFetching = createSelector(
  getFetchingState,
  fetching => fetching
);
