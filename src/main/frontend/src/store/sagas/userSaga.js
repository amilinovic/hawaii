import { actions as toastrActions } from 'react-redux-toastr';
import { call, put, takeEvery } from 'redux-saga/effects';
import {
  errorReceivingUser,
  receiveUser,
  requestUser
} from '../actions/userActions';
import { getUserApi } from '../services/userService';

export const getUser = function*(action) {
  try {
    const userInformation = yield call(getUserApi, action.payload);
    yield put(receiveUser(userInformation));
  } catch (error) {
    yield put(errorReceivingUser(error));
    yield console.log(error.message);
    yield put(
      toastrActions.add({
        type: 'error',
        title: 'Error',
        message: error.message
      })
    );
  }
};

export const userSaga = [takeEvery(requestUser, getUser)];
