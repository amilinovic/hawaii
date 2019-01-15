import { call, put, takeEvery } from 'redux-saga/effects';
import {
  requestUser,
  receiveUser,
  errorReceivingUser
} from '../actions/userActions';
import { getUserApi } from '../services/userService';

export const getUser = function*(action) {
  try {
    const userInformation = yield call(getUserApi, action.payload);
    yield put(receiveUser(userInformation));
  } catch (error) {
    yield put(errorReceivingUser(error));
  }
};

export const userSaga = [takeEvery(requestUser, getUser)];
