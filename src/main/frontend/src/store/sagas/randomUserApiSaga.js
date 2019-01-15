import { call, put, takeLatest } from 'redux-saga/effects';
import {
  requestApiData,
  receiveApiData,
  errorReceivingApiData
} from '../actions/randomUserApiActions';
import { randomUserApiRequest } from '../services/randomUserApiRequest';

export const getApiData = function*() {
  try {
    const employeeInformation = yield call(randomUserApiRequest);
    yield put(receiveApiData(employeeInformation));
  } catch (error) {
    yield put(errorReceivingApiData(error));
  }
};

export const randomUserApiSaga = [takeLatest(requestApiData, getApiData)];
