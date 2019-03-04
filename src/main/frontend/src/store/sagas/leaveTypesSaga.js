import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingLeaveTypes,
  receiveLeaveTypes,
  requestLeaveTypes
} from '../actions/leaveTypesActions';
import { getLeaveTypesService } from '../services/leaveTypesService';
import {
  errorHandlingAction,
  withErrorHandling
} from './HOC/withErrorHandling';

export const getAllLeaveTypes = function*() {
  const leaveTypes = yield call(getLeaveTypesService);
  yield put(receiveLeaveTypes(leaveTypes));
};

export const leaveTypesSaga = [
  takeLatest(
    requestLeaveTypes,
    withErrorHandling(getAllLeaveTypes, () =>
      errorHandlingAction(errorReceivingLeaveTypes)
    )
  )
];
