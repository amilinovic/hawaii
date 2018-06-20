import { call, put, takeLatest } from 'redux-saga/effects';

import { REQUEST_API_DATA, receiveApiData } from '../actions/EmployeesActions';

import { fetchData } from '../services/user';

function* getApiData() {
  try {
    const data = yield call(fetchData);
    yield put(receiveApiData(data));
  } catch (e) {
    // TODO error handling
    console.log(e);
  }
}

export default function* mySaga() {
  yield takeLatest(REQUEST_API_DATA, getApiData);
}
