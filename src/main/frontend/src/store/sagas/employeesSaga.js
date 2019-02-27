import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingEmployees,
  receiveEmployees,
  requestEmployees
} from '../actions/employeesActions';
import { getEmployeesApi } from '../services/employeesService';
import { toastrError } from './toastrHelperSaga';

export const getEmployees = function*() {
  try {
    const employeesInformation = yield call(getEmployeesApi);
    yield put(receiveEmployees(employeesInformation));
  } catch (error) {
    yield put(errorReceivingEmployees(error));
    yield put(toastrError(error.message));
  }
};

export const employeesSaga = [takeLatest(requestEmployees, getEmployees)];
