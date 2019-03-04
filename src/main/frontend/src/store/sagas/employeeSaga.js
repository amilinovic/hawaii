import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  createEmployee,
  createEmployeeSuccess,
  errorCreatingEmployee,
  errorReceivingEmployee,
  errorRemovingEmployee,
  receiveEmployee,
  removeEmployee,
  removeEmployeeSuccess,
  requestEmployee,
  updateEmployee,
  updateEmployeeError,
  updateEmployeeSuccessful
} from '../actions/employeeActions';
import {
  createEmployeeApi,
  getEmployeeApi,
  removeEmployeeApi,
  updateEmployeeApi
} from '../services/employeeService';
import {
  errorHandlingAction,
  withErrorHandling
} from './HOC/withErrorHandling';

export const createEmployeeSaga = function*(action) {
  yield call(createEmployeeApi(action.payload));
  yield put(createEmployeeSuccess());
};

export const getEmployeeSaga = function*(action) {
  const employeeInformation = yield call(getEmployeeApi(action.payload));
  yield put(receiveEmployee(employeeInformation));
};

export const updateEmployeeSaga = function*(action) {
  yield call(updateEmployeeApi(action.payload));
  yield put(updateEmployeeSuccessful());
};

export const removeEmployeeSaga = function*(action) {
  yield call(removeEmployeeApi(action.payload.id));
  yield put(removeEmployeeSuccess());
  yield put(push('/administration'));
};

export const employeeSaga = [
  takeLatest(
    requestEmployee,
    withErrorHandling(getEmployeeSaga, () =>
      errorHandlingAction(errorReceivingEmployee)
    )
  ),
  takeLatest(
    updateEmployee,
    withErrorHandling(updateEmployeeSaga, () =>
      errorHandlingAction(updateEmployeeError)
    )
  ),
  takeLatest(
    createEmployee,
    withErrorHandling(createEmployeeSaga, () =>
      errorHandlingAction(errorCreatingEmployee)
    )
  ),
  takeLatest(
    removeEmployee,
    withErrorHandling(removeEmployeeSaga, () =>
      errorHandlingAction(errorRemovingEmployee)
    )
  )
];
