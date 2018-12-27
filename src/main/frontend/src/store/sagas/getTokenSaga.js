import { call, put, takeEvery } from 'redux-saga/effects';
import {
  REQUEST_TOKEN,
  receiveToken,
  receiveTokenError
} from '../actions/getTokenActions';
import { tokenRequest } from '../services/getTokenRequest';

export const getToken = function*(action) {
  try {
    const responseGoogle = yield call(tokenRequest, action.payload);
    yield put(receiveToken(responseGoogle));
  } catch (error) {
    yield put(receiveTokenError(error));
  }
};

export const getTokenSaga = [takeEvery(REQUEST_TOKEN, getToken)];
