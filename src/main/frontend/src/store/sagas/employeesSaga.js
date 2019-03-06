import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingEmployees,
  receiveEmployees,
  requestEmployees
} from '../actions/employeesActions';
import { getEmployeesApi } from '../services/employeesService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';

export const getEmployees = function*() {
  const employeesInformation = yield call(getEmployeesApi);
  yield put(receiveEmployees(employeesInformation));
};

export const employeesSaga = [
  takeLatest(
    requestEmployees,
    withErrorHandling(getEmployees, e =>
      genericErrorHandler(errorReceivingEmployees(e))
    )
  )
];
