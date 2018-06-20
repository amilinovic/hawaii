import { call, put, takeLatest } from 'redux-saga/effects';

import { receiveApiData } from '../actions/EmployeesActions';
import { REQUEST_API_DATA } from '../actions/types';

import { fetchData } from './api';

function* getApiData() {
  try {
    const data = yield call(fetchData);
    yield put(receiveApiData(data));
  } catch (e) {
    console.log(e);
  }
}

export default function* mySaga() {
  yield takeLatest(REQUEST_API_DATA, getApiData);
}
