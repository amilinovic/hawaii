import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingPublicHolidays,
  receivePublicHolidays,
  requestPublicHolidays
} from '../actions/publicHolidaysActions';
import { getPublicHolidaysApi } from '../services/publicHolidaysService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';

export const getPublicHolidays = function*() {
  const publicHolidays = yield call(getPublicHolidaysApi);
  yield put(receivePublicHolidays(publicHolidays));
};

export const publicHolidaysSaga = [
  takeLatest(
    requestPublicHolidays,
    withErrorHandling(
      getPublicHolidays,
      genericErrorHandler(errorReceivingPublicHolidays)
    )
  )
];
