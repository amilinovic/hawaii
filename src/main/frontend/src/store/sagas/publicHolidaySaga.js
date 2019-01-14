import { call, put, takeLatest } from 'redux-saga/effects';
import {
  receivePublicHolidays,
  errorReceivingPublicHolidays,
  requestPublicHolidays
} from '../actions/publicHolidayActions';
import { getPublicHolidaysApi } from '../services/publicHolidaysApi';

export const getPublicHolidays = function*() {
  try {
    const publicHolidays = yield call(getPublicHolidaysApi);
    yield put(receivePublicHolidays(publicHolidays));
  } catch (error) {
    yield put(errorReceivingPublicHolidays(error));
  }
};

export const publicHolidaysSaga = [
  takeLatest(requestPublicHolidays, getPublicHolidays)
];
