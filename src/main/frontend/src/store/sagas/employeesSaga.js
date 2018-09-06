import * as action from '../actions/employeesActions';
import { call, put, takeLatest } from 'redux-saga/effects';

import request from 'superagent';

export const fetchEmployeesFromServer = () => {
  try {
    return request
      .get('/users')
      .then(res => res.body)
      .catch(err => {
        console.log(err);
      });
  } catch (error) {
    console.log(error);
  }
  return false;
};

export const fetchEmployees = function*() {
  try {
    const employees = yield call(fetchEmployeesFromServer);
    console.log('Employees from the server', employees);
    yield put(action.requestEmployeesSucceeded(employees));
  } catch (error) {
    yield put(action.requestEmployeesFailed(error.message));
  }
};

export const fetchEmployeesSaga = function*() {
  yield* takeLatest(action.requestEmployees, fetchEmployees);
};

export default [fetchEmployeesSaga];
