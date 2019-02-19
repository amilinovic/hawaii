import { call, put, takeLatest } from 'redux-saga/effects';
import {
  createEmployee,
  createEmployeeSuccess,
  errorCreatingEmployee,
  errorReceivingEmployee,
  receiveEmployee,
  requestEmployee,
  updateEmployee,
  updateEmployeeError,
  updateEmployeeSuccessful
} from '../actions/employeeActions';
import {
  createEmployeeApi,
  getEmployeeApi,
  updateEmployeeApi
} from '../services/employeeService';

export const createEmployeeSaga = function*(action) {
  try {
    yield call(createEmployeeApi, action.payload);
    yield put(createEmployeeSuccess());
  } catch (error) {
    yield put(errorCreatingEmployee(error));
  }
};

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
  takeLatest(updateEmployee, updateEmployeeSaga),
  takeLatest(createEmployee, createEmployeeSaga)
];
