import { call, put, takeLatest } from 'redux-saga/effects';
import {
  clearEmployees,
  errorSearchingEmployees,
  searchEmployees,
  searchEmployeesSuccess
} from '../actions/employeesSearchActions';
import { searchEmployeesApi } from '../services/employeesService';

export const searchEmployeesSaga = function*(action) {
  if (action.payload) {
    try {
      const employeesInformation = yield call(
        searchEmployeesApi(action.payload)
      );
      yield put(searchEmployeesSuccess(employeesInformation));
    } catch (error) {
      yield put(errorSearchingEmployees(error));
    }
  } else {
    yield put(clearEmployees());
  }
};

export const employeesSearchSaga = [
  takeLatest(searchEmployees, searchEmployeesSaga)
];
