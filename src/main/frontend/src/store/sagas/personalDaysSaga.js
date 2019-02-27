import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingPersonalDays,
  receivePersonalDays,
  requestPersonalDays
} from '../actions/personalDaysActions';
import { getPersonalDaysApi } from '../services/personalDayService';

export const getPersonalDays = function*() {
  try {
    const personalDays = yield call(getPersonalDaysApi);
    yield put(receivePersonalDays(personalDays));
  } catch (error) {
    yield put(errorReceivingPersonalDays(error));
  }
};

export const personalDaysSaga = [
  takeLatest(requestPersonalDays, getPersonalDays)
];
