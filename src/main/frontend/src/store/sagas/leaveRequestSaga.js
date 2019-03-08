import { call, put, takeLatest } from 'redux-saga/effects';
import {
  createLeaveRequest,
  createLeaveRequestError,
  createLeaveRequestSuccessful
} from '../actions/leaveRequestActions';
import { closeModal } from '../actions/modalActions';
import { requestPersonalDays } from '../actions/personalDaysActions';
import { createLeaveRequestApi } from '../services/leaveRequestService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';
import { toastrSuccess } from './toastrHelperSaga';

export const createLeaveRequestSaga = function*(action) {
  yield call(createLeaveRequestApi(action.payload));
  yield put(createLeaveRequestSuccessful());
  yield put(toastrSuccess('Leave request successfuly created'));
  yield put(requestPersonalDays());
  yield put(closeModal());
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
