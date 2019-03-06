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
import { toastrError, toastrSuccess } from './toastrHelperSaga';

export const createEmployeeSaga = function*(action) {
  try {
    yield call(createEmployeeApi(action.payload));
    yield put(createEmployeeSuccess());
    yield put(toastrSuccess('Succesfully created employee'));
    yield put(push('/administration'));
  } catch (error) {
    yield put(errorCreatingEmployee(error));
    yield put(toastrError(error.message));
  }
};

export const getEmployeeSaga = function*(action) {
  try {
    const employeeInformation = yield call(getEmployeeApi(action.payload));
    yield put(receiveEmployee(employeeInformation));
  } catch (error) {
    yield put(errorReceivingEmployee(error));
    yield put(toastrError(error.message));
  }
};

export const updateEmployeeSaga = function*(action) {
  try {
    yield call(updateEmployeeApi(action.payload));
    yield put(updateEmployeeSuccessful());
    yield put(toastrSuccess('Succesfully updated employee'));
    yield put(push(`/employee/${action.payload.id}`));
  } catch (error) {
    yield put(updateEmployeeError(error));
    yield put(toastrError(error.message));
  }
};

export const removeEmployeeSaga = function*(action) {
  try {
    yield call(removeEmployeeApi(action.payload.id));
    yield put(removeEmployeeSuccess());
    yield put(toastrSuccess('Succesfully deleted employee'));
    yield put(push('/administration'));
  } catch (error) {
    yield put(errorRemovingEmployee(error));
    yield put(toastrError(error.message));
  }
};

export const employeeSaga = [
  takeLatest(requestEmployee, getEmployeeSaga),
  takeLatest(updateEmployee, updateEmployeeSaga),
  takeLatest(createEmployee, createEmployeeSaga),
  takeLatest(removeEmployee, removeEmployeeSaga)
];
