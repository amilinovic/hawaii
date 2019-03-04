import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingEmployees,
  receiveEmployees,
  requestEmployees
} from '../actions/employeesActions';
import { getEmployeesApi } from '../services/employeesService';
import {
  errorHandlingAction,
  withErrorHandling
} from './HOC/withErrorHandling';

export const getEmployees = function*() {
  const employeesInformation = yield call(getEmployeesApi);
  yield put(receiveEmployees(employeesInformation));
};

export const employeesSaga = [
  takeLatest(
    requestEmployees,
    withErrorHandling(getEmployees, () =>
      errorHandlingAction(errorReceivingEmployees)
    )
  )
];
