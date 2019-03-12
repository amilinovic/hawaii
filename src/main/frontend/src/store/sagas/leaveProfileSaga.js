import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  createLeaveProfile,
  createLeaveProfileError,
  createLeaveProfileSuccessful,
  errorReceivingLeaveProfile,
  errorRemovingLeaveProfile,
  receiveLeaveProfile,
  removeLeaveProfile,
  removeLeaveProfileSuccess,
  requestLeaveProfile
} from '../actions/leaveProfileActions';
import {
  createLeaveProfileApi,
  getLeaveProfileApi,
  removeLeaveProfileApi
} from '../services/leaveProfileService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';
import { toastrSuccess } from './toastrHelperSaga';

const getLeaveProfileSaga = function*(action) {
  const leaveProfile = yield call(getLeaveProfileApi(action.payload));
  yield put(receiveLeaveProfile(leaveProfile));
};

const removeLeaveProfileSaga = function*(action) {
  yield call(removeLeaveProfileApi(action.payload.id));
  yield put(removeLeaveProfileSuccess());
  yield put(toastrSuccess('Succesfully removed leave profile'));
  yield put(push('/administration'));
};

const createLeaveProfileSaga = function*(action) {
  yield call(createLeaveProfileApi(action.payload));
  yield put(createLeaveProfileSuccessful());
  yield put(push('/administration'));
  yield put(toastrSuccess('Successfully created LeaveProfile'));
};

export const leaveProfileSaga = [
  takeLatest(
    requestLeaveProfile,
    withErrorHandling(
      getLeaveProfileSaga,
      genericErrorHandler(errorReceivingLeaveProfile)
    )
  ),
  takeLatest(
    removeLeaveProfile,
    withErrorHandling(
      removeLeaveProfileSaga,
      genericErrorHandler(errorRemovingLeaveProfile)
    )
  ),
  takeLatest(
    createLeaveProfile,
    withErrorHandling(
      createLeaveProfileSaga,
      genericErrorHandler(createLeaveProfileError)
    )
  )
];
