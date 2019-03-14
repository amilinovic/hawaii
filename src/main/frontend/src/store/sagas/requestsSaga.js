import { call, put, takeLatest } from 'redux-saga/effects';
import { closeModal } from '../actions/modalActions';
import { requestPersonalDays } from '../actions/personalDaysActions';
import {
  createBonusRequest,
  createBonusRequestError,
  createBonusRequestSuccessful,
  createLeaveRequest,
  createLeaveRequestError,
  createLeaveRequestSuccessful,
  createSicknessRequest,
  createSicknessRequestError,
  createSicknessRequestSuccessful
} from '../actions/requestsActions';
import { requestApi } from '../services/requestsService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';
import { toastrSuccess } from './toastrHelperSaga';

export const createLeaveRequestSaga = function*(action) {
  yield call(requestApi(action.payload));
  yield put(createLeaveRequestSuccessful());
  yield put(toastrSuccess('Leave request successfuly created'));
  yield put(requestPersonalDays());
  yield put(closeModal());
};

export const createSicknessRequestSaga = function*(action) {
  yield call(requestApi(action.payload));
  yield put(createSicknessRequestSuccessful());
  yield put(toastrSuccess('Sickness request successfuly created'));
  yield put(requestPersonalDays());
  yield put(closeModal());
};

export const createBonusRequestSaga = function*(action) {
  yield call(requestApi(action.payload));
  yield put(createBonusRequestSuccessful());
  yield put(toastrSuccess('Bonus request successfuly created'));
  yield put(requestPersonalDays());
  yield put(closeModal());
};

export const requestsSaga = [
  takeLatest(
    createLeaveRequest,
    withErrorHandling(
      createLeaveRequestSaga,
      genericErrorHandler(createLeaveRequestError)
    )
  ),
  takeLatest(
    createSicknessRequest,
    withErrorHandling(
      createSicknessRequestSaga,
      genericErrorHandler(createSicknessRequestError)
    )
  ),
  takeLatest(
    createBonusRequest,
    withErrorHandling(
      createBonusRequestSaga,
      genericErrorHandler(createBonusRequestError)
    )
  )
];
