import { push } from 'connected-react-router';
import { call, put, takeEvery } from 'redux-saga/effects';
import {
  receiveToken,
  receiveTokenError,
  REQUEST_TOKEN
} from '../actions/getTokenActions';
import { tokenRequest } from '../services/getTokenRequest';

export const getToken = function*(action) {
  try {
    const responseGoogle = yield call(tokenRequest, action.payload);
    yield put(receiveToken(responseGoogle));
  } catch (error) {
    yield put(receiveTokenError(error));
    if (error.status === 401) {
      yield put(push('/login'));
    }
  }
};

export const getTokenSaga = [takeEvery(REQUEST_TOKEN, getToken)];
