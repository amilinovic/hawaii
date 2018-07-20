import { call, put, all, takeLatest } from 'redux-saga/effects';
import {
  REQUEST_API_DATA,
  receiveApiData,
  errorReceivingApiData
} from '../actions/RandomUserApiActions';
import { randomUserApiRequest } from '../services/randomUserApiRequest';

export const getApiData = function*() {
  try {
    const employeeInformation = yield call(randomUserApiRequest);
    yield put(receiveApiData(employeeInformation));
  } catch (error) {
    yield put(errorReceivingApiData(error));
  }
};

export default function* randomUserApiSaga() {
  yield all([takeLatest(REQUEST_API_DATA, getApiData)]);
}
