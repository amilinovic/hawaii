import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingPublicHolidays,
  receivePublicHolidays,
  requestPublicHolidays
} from '../actions/publicHolidayActions';
import { getPublicHolidaysApi } from '../services/publicHolidaysService';

export const getPublicHolidays = function*() {
  try {
    const publicHolidays = yield call(getPublicHolidaysApi);
    yield put(receivePublicHolidays(publicHolidays));
  } catch (error) {
    yield put(errorReceivingPublicHolidays(error));
    if (error.status === 401) {
      yield put(push('/login'));
    }
  }
};

export const publicHolidaysSaga = [
  takeLatest(requestPublicHolidays, getPublicHolidays)
];
