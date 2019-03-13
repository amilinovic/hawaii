import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  createYear,
  createYearError,
  createYearSuccessful,
  errorReceivingYear,
  errorRemovingYear,
  receiveYear,
  removeYear,
  removeYearSuccess,
  requestYear,
  updateYear,
  updateYearError,
  updateYearSuccessful
} from '../actions/yearActions';
import {
  createYearApi,
  getYearApi,
  removeYearApi,
  updateYearApi
} from '../services/yearService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';
import { toastrSuccess } from './toastrHelperSaga';

const getYearSaga = function*(action) {
  const yearInformation = yield call(getYearApi(action.payload));
  yield put(receiveYear(yearInformation));
};

const removeYearSaga = function*(action) {
  yield call(removeYearApi(action.payload.id));
  yield put(removeYearSuccess());
  yield put(toastrSuccess('Succesfully removed year'));
  yield put(push('/administration'));
};

const updateYearSaga = function*(action) {
  yield call(updateYearApi(action.payload));
  yield put(updateYearSuccessful());
  yield put(toastrSuccess('Succesfully updated year'));
};

const createYearSaga = function*(action) {
  yield call(createYearApi(action.payload));
  yield put(createYearSuccessful());
  yield put(push('/administration'));
  yield put(toastrSuccess('Successfully created year'));
};

export const yearSaga = [
  takeLatest(
    requestYear,
    withErrorHandling(getYearSaga, genericErrorHandler(errorReceivingYear))
  ),
  takeLatest(
    updateYear,
    withErrorHandling(updateYearSaga, genericErrorHandler(updateYearError))
  ),
  takeLatest(
    removeYear,
    withErrorHandling(removeYearSaga, genericErrorHandler(errorRemovingYear))
  ),
  takeLatest(
    createYear,
    withErrorHandling(createYearSaga, genericErrorHandler(createYearError))
  )
];
