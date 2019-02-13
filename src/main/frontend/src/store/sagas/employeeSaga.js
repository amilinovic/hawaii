import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingEmployee,
  receiveEmployee,
  requestEmployee,
  updateEmployee,
  updateEmployeeError,
  updateEmployeeSuccessful
} from '../actions/employeeActions';
import { getEmployeeApi, updateEmployeeApi } from '../services/employeeService';

export const getEmployeeSaga = function*(action) {
  try {
    const employeeInformation = yield call(getEmployeeApi, action.payload);
    yield put(receiveEmployee(employeeInformation));
  } catch (error) {
    yield put(errorReceivingEmployee(error));
  }
};

export const updateEmployeeSaga = function*(action) {
  try {
    yield call(updateEmployeeApi, action.payload);
    yield put(updateEmployeeSuccessful());
  } catch (error) {
    yield put(updateEmployeeError(error));
  }
};

export const employeeSaga = [
  takeLatest(requestEmployee, getEmployeeSaga),
  takeLatest(updateEmployee, updateEmployeeSaga)
];
