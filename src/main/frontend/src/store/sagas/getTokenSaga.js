import { call, put, takeEvery } from 'redux-saga/effects';
import {
  receiveToken,
  receiveTokenError,
  requestToken
} from '../actions/getTokenActions';
import { tokenRequest } from '../services/getTokenRequest';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';

export const getToken = function*(action) {
  const responseGoogle = yield call(tokenRequest, action.payload);
  yield put(receiveToken(responseGoogle));
};

export const getTokenSaga = [
  takeEvery(
    requestToken,
    withErrorHandling(getToken, genericErrorHandler(receiveTokenError))
  )
];
