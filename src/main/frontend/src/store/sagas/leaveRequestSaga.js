import { call, put, takeLatest } from 'redux-saga/effects';
import {
  createLeaveRequest,
  createLeaveRequestError,
  createLeaveRequestSuccessful
} from '../actions/leaveRequestActions';
import { createLeaveRequestApi } from '../services/leaveRequestService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';

export const createLeaveRequestSaga = function*(action) {
  yield call(createLeaveRequestApi);
  yield put(createLeaveRequestSuccessful());
};

export const leaveRequestSaga = [
  takeLatest(
    createLeaveRequest,
    withErrorHandling(
      createLeaveRequestSaga,
      genericErrorHandler(createLeaveRequestError)
    )
  )
];
