import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingEmployee,
  errorRemovingEmployee,
  receiveEmployee,
  removeEmployee,
  removeEmployeeSuccess,
  requestEmployee
} from '../actions/employeeActions';
import { getEmployeeApi, removeEmployeeApi } from '../services/employeeService';

export const getEmployeeSaga = function*(action) {
  try {
    const employeeInformation = yield call(getEmployeeApi, action.payload);
    yield put(receiveEmployee(employeeInformation));
  } catch (error) {
    yield put(errorReceivingEmployee(error));
  }
};

export const removeEmployeeSaga = function*(action) {
  try {
    yield call(removeEmployeeApi, action.payload.id);
    yield put(removeEmployeeSuccess());
    yield put(push('/administration'));
  } catch (error) {
    yield put(errorRemovingEmployee(error));
  }
};

export const employeeSaga = [
  takeLatest(requestEmployee, getEmployeeSaga),
  takeLatest(removeEmployee, removeEmployeeSaga)
];
