import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingUser,
  receiveUser,
  requestUser
} from '../actions/userActions';
import { getUserApi } from '../services/userService';
import {
  errorHandlingAction,
  withErrorHandling
} from './HOC/withErrorHandling';

const getUserSaga = function*(action) {
  const userInformation = yield call(getUserApi, action.payload);
  yield put(receiveUser(userInformation));
};

export const userSaga = [
  takeLatest(
    requestUser,
    withErrorHandling(getUserSaga, () =>
      errorHandlingAction(errorReceivingUser)
    )
  )
];
