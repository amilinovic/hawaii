import { createSelector } from 'reselect';
import { identity } from 'lodash';

const getEmployeeState = state => state.employee.employeeInformation;
const getFetchingState = state => state.employee.fetching;

export const getEmployee = createSelector(getEmployeeState, employee =>
  identity(employee)
);

export const getFetching = createSelector(getFetchingState, fetching =>
  identity(fetching)
);
