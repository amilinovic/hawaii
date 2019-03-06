import { call, put, takeEvery } from 'redux-saga/effects';
import {
  errorReceivingUser,
  receiveUser,
  requestUser
} from '../actions/userActions';
import { getUserApi } from '../services/userService';
import { toastrError } from './toastrHelperSaga';

export const getUser = function*(action) {
  try {
    const userInformation = yield call(getUserApi, action.payload);
    yield put(receiveUser(userInformation));
  } catch (error) {
    yield put(errorReceivingUser(error));
    yield put(toastrError(error.message));
  }
};

export const userSaga = [takeEvery(requestUser, getUser)];
