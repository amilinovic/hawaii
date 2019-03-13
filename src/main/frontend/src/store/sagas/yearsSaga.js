import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingYears,
  receiveYears,
  requestYears
} from '../actions/yearsActions';
import { getYearsApi } from '../services/yearsService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';

const getAllYearsSaga = function*() {
  const yearsInformation = yield call(getYearsApi);
  yield put(receiveYears(yearsInformation));
};

export const yearsSaga = [
  takeLatest(
    requestYears,
    withErrorHandling(getAllYearsSaga, genericErrorHandler(errorReceivingYears))
  )
];
