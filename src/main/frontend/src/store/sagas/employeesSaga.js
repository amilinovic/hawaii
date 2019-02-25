import { call, put, takeLatest } from 'redux-saga/effects';
import {
  clearEmployees,
  errorReceivingEmployees,
  receiveEmployees,
  requestEmployees,
  searchEmployees
} from '../actions/employeesActions';
import {
  getEmployeesApi,
  searchEmployeesApi
} from '../services/employeesService';

export const getEmployees = function*() {
  try {
    const employeesInformation = yield call(getEmployeesApi);
    yield put(receiveEmployees(employeesInformation));
  } catch (error) {
    yield put(errorReceivingEmployees(error));
  }
};

export const searchEmployeesSaga = function*(action) {
  if (action.payload) {
    try {
      const employeesInformation = yield call(
        searchEmployeesApi,
        action.payload
      );
      yield put(receiveEmployees(employeesInformation));
    } catch (error) {
      yield put(errorReceivingEmployees(error));
    }
  } else {
    yield put(clearEmployees());
  }
};

export const employeesSaga = [
  takeLatest(requestEmployees, getEmployees),
  takeLatest(searchEmployees, searchEmployeesSaga)
];
