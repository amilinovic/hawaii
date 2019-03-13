import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  createPublicHoliday,
  createPublicHolidayError,
  createPublicHolidaySuccessful,
  errorReceivingPublicHoliday,
  errorRemovingPublicHoliday,
  receivePublicHoliday,
  removePublicHoliday,
  removePublicHolidaySuccess,
  requestPublicHoliday,
  updatePublicHoliday,
  updatePublicHolidayError,
  updatePublicHolidaySuccessful
} from '../actions/publicHolidayActions';
import {
  createPublicHolidayApi,
  getPublicHolidayApi,
  removePublicHolidayApi,
  updatePublicHolidayApi
} from '../services/publicHolidayService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';
import { toastrSuccess } from './toastrHelperSaga';

const getPublicHolidaySaga = function*(action) {
  const publicHolidayInformation = yield call(
    getPublicHolidayApi(action.payload)
  );
  yield put(receivePublicHoliday(publicHolidayInformation));
};

const removePublicHolidaySaga = function*(action) {
  yield call(removePublicHolidayApi(action.payload.id));
  yield put(removePublicHolidaySuccess());
  yield put(toastrSuccess('Succesfully removed public holiday'));
  yield put(push('/administration'));
};

const updatePublicHolidaySaga = function*(action) {
  yield call(updatePublicHolidayApi(action.payload));
  yield put(updatePublicHolidaySuccessful());
  yield put(toastrSuccess('Succesfully updated public holiday'));
};

const createPublicHolidaySaga = function*(action) {
  yield call(createPublicHolidayApi(action.payload));
  yield put(createPublicHolidaySuccessful());
  yield put(push('/administration'));
  yield put(toastrSuccess('Successfully created public holiday'));
};

export const publicHolidaySaga = [
  takeLatest(
    requestPublicHoliday,
    withErrorHandling(
      getPublicHolidaySaga,
      genericErrorHandler(errorReceivingPublicHoliday)
    )
  ),
  takeLatest(
    updatePublicHoliday,
    withErrorHandling(
      updatePublicHolidaySaga,
      genericErrorHandler(updatePublicHolidayError)
    )
  ),
  takeLatest(
    removePublicHoliday,
    withErrorHandling(
      removePublicHolidaySaga,
      genericErrorHandler(errorRemovingPublicHoliday)
    )
  ),
  takeLatest(
    createPublicHoliday,
    withErrorHandling(
      createPublicHolidaySaga,
      genericErrorHandler(createPublicHolidayError)
    )
  )
];
