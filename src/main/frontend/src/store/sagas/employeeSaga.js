import { call, put, takeLatest } from 'redux-saga/effects';
import {
  createEmployee,
  createEmployeeSuccess,
  errorCreatingEmployee
} from '../actions/employeeActions';
import { createEmployeeApi } from '../services/employeeService';

export const createEmployeeSaga = function*(action) {
  try {
    yield call(createEmployeeApi, action.payload);
    yield put(createEmployeeSuccess());
  } catch (error) {
    yield put(errorCreatingEmployee(error));
  }
};

export const employeeSaga = [takeLatest(createEmployee, createEmployeeSaga)];
