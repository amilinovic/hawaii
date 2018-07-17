import { call, put, takeLatest } from 'redux-saga/effects';

import {
  REQUEST_API_DATA,
  receiveApiData,
  errorReceivingApiData
} from '../actions/EmployeesActions';

import { fetchData } from '../services/user';

export const getApiData = function*() {
  try {
    const employeeInformation = yield call(fetchData);
    yield put(receiveApiData(employeeInformation));
  } catch (error) {
    yield put(errorReceivingApiData(error));
  }
};

export default function* mySaga() {
  yield takeLatest(REQUEST_API_DATA, getApiData);
}
