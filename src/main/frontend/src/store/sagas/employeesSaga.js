import { call, put, takeEvery } from 'redux-saga/effects';
import {
  requestEmployees,
  receiveEmployees,
  errorReceivingEmployees
} from '../actions/employeesAction';
import { getEmployeesApi } from '../services/employeesService';

export const getEmployees = function*(action) {
  try {
    const employeesInformation = yield call(getEmployeesApi, action.payload);
    yield put(receiveEmployees(employeesInformation));
  } catch (error) {
    yield put(errorReceivingEmployees(error));
  }
};

export const employeesSaga = [takeEvery(requestEmployees, getEmployeesApi)];
