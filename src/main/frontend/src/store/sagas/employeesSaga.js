import { call, put, takeLatest } from 'redux-saga/effects';
import {
  requestEmployees,
  receiveEmployees,
  errorReceivingEmployees
} from '../actions/employeesAction';
import { getEmployeesApi } from '../services/employeesService';

export const getEmployees = function*() {
  try {
    const employeesInformation = yield call(getEmployeesApi);
    yield put(receiveEmployees(employeesInformation));
  } catch (error) {
    yield put(errorReceivingEmployees(error));
  }
};

export const employeesSaga = [takeLatest(requestEmployees, getEmployees)];
