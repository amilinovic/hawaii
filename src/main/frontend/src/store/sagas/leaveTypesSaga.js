import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingLeaveTypes,
  receiveLeaveTypes,
  requestLeaveTypes
} from '../actions/leaveTypesActions';
import { getLeaveTypesService } from '../services/leaveTypesService';

export const getAllLeaveTypes = function*() {
  try {
    const leaveTypes = yield call(getLeaveTypesService);
    yield put(receiveLeaveTypes(leaveTypes));
  } catch (error) {
    yield put(errorReceivingLeaveTypes(error));
    if (error.status === 401) {
      yield put(push('/login'));
    }
  }
};

export const leaveTypesSaga = [takeLatest(requestLeaveTypes, getAllLeaveTypes)];
