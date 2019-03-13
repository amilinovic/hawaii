import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingLeaveProfiles,
  receiveLeaveProfiles,
  requestLeaveProfiles
} from '../actions/leaveProfilesActions';
import { getLeaveProfilesApi } from '../services/leaveProfilesService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';

export const getAllLeaveProfiles = function*() {
  const leaveProfiles = yield call(getLeaveProfilesApi);
  yield put(receiveLeaveProfiles(leaveProfiles));
};

export const leaveProfilesSaga = [
  takeLatest(
    requestLeaveProfiles,
    withErrorHandling(
      getAllLeaveProfiles,
      genericErrorHandler(errorReceivingLeaveProfiles)
    )
  )
];
