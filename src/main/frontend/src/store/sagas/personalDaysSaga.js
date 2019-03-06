import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingPersonalDays,
  receivePersonalDays,
  requestPersonalDays
} from '../actions/personalDaysActions';
import { getPersonalDaysApi } from '../services/personalDayService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';

export const getPersonalDays = function*() {
  const personalDays = yield call(getPersonalDaysApi);
  yield put(receivePersonalDays(personalDays));
};

export const personalDaysSaga = [
  takeLatest(
    requestPersonalDays,
    withErrorHandling(getPersonalDays, e =>
      genericErrorHandler(errorReceivingPersonalDays(e))
    )
  )
];
