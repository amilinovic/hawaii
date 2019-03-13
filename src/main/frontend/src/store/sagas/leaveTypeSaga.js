import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  createLeaveType,
  createLeaveTypeError,
  createLeaveTypeSuccessful,
  errorReceivingLeaveType,
  errorRemovingLeaveType,
  receiveLeaveType,
  removeLeaveType,
  removeLeaveTypeSuccess,
  requestLeaveType,
  updateLeaveType,
  updateLeaveTypeError,
  updateLeaveTypeSuccessful
} from '../actions/leaveTypeActions';
import {
  createLeaveTypeApi,
  getLeaveTypeApi,
  removeLeaveTypeApi,
  updateLeaveTypeApi
} from '../services/leaveTypeService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';
import { toastrSuccess } from './toastrHelperSaga';

const getLeaveTypeSaga = function*(action) {
  const leaveTypeInformation = yield call(getLeaveTypeApi(action.payload));
  yield put(receiveLeaveType(leaveTypeInformation));
};

const removeLeaveTypeSaga = function*(action) {
  yield call(removeLeaveTypeApi(action.payload.id));
  yield put(removeLeaveTypeSuccess());
  yield put(toastrSuccess('Succesfully removed leave type'));
  yield put(push('/administration'));
};

const updateLeaveTypeSaga = function*(action) {
  yield call(updateLeaveTypeApi(action.payload));
  yield put(updateLeaveTypeSuccessful());
  yield put(toastrSuccess('Succesfully updated leave type'));
};

const createLeaveTypeSaga = function*(action) {
  yield call(createLeaveTypeApi(action.payload));
  yield put(createLeaveTypeSuccessful());
  yield put(push('/administration'));
  yield put(toastrSuccess('Successfully created leave type'));
};

export const leaveTypeSaga = [
  takeLatest(
    requestLeaveType,
    withErrorHandling(
      getLeaveTypeSaga,
      genericErrorHandler(errorReceivingLeaveType)
    )
  ),
  takeLatest(
    updateLeaveType,
    withErrorHandling(
      updateLeaveTypeSaga,
      genericErrorHandler(updateLeaveTypeError)
    )
  ),
  takeLatest(
    removeLeaveType,
    withErrorHandling(
      removeLeaveTypeSaga,
      genericErrorHandler(errorRemovingLeaveType)
    )
  ),
  takeLatest(
    createLeaveType,
    withErrorHandling(
      createLeaveTypeSaga,
      genericErrorHandler(createLeaveTypeError)
    )
  )
];
