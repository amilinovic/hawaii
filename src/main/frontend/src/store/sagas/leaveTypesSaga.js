import { call, put, takeLatest } from 'redux-saga/effects';
import {
  receiveLeaveTypes,
  requestLeaveTypes,
  errorReceivingLeaveTypes
} from '../actions/leaveTypesActions';
import { getLeaveTypesService } from '../services/leaveTypesService';

export const getAllLeaveTypes = function*() {
  try {
    const leaveTypes = yield call(getLeaveTypesService);
    yield put(receiveLeaveTypes(leaveTypes));
  } catch (error) {
    yield put(errorReceivingLeaveTypes(error));
  }
};

export const leaveTypesSaga = [takeLatest(requestLeaveTypes, getAllLeaveTypes)];
