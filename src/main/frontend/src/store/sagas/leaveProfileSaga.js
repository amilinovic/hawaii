import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingLeaveProfile,
  receiveLeaveProfile,
  requestLeaveProfile
} from '../actions/leaveProfileActions';
import { getLeaveProfileApi } from '../services/leaveProfileService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';

export const getLeaveProfile = function*(action) {
  const leaveProfile = yield call(getLeaveProfileApi(action.payload));
  yield put(receiveLeaveProfile(leaveProfile));
};

export const leaveProfileSaga = [
  takeLatest(
    requestLeaveProfile,
    withErrorHandling(
      getLeaveProfile,
      genericErrorHandler(errorReceivingLeaveProfile)
    )
  )
];
