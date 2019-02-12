import { call, put, takeEvery } from 'redux-saga/effects';
import {
  errorReceivingUsers,
  receiveUsers,
  requestUsers
} from '../actions/usersActions';
import { getUsersApi } from '../services/usersService';

export const getUsers = function*(action) {
  try {
    const usersInformation = yield call(getUsersApi, action.payload);
    yield put(receiveUsers(usersInformation));
  } catch (error) {
    yield put(errorReceivingUsers(error));
  }
};

export const usersSaga = [takeEvery(requestUsers, getUsers)];
