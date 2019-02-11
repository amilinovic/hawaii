import { push } from 'connected-react-router';
import { call, put, takeEvery } from 'redux-saga/effects';
import { receiveToken } from '../actions/getTokenActions';
import {
  receiveTokenFromStorageError,
  REQUEST_TOKEN_FROM_STORAGE
} from '../actions/getTokenFromSessionStorageActions';
import { getTokenFromSessionStorage } from '../services/getTokenFromSessionStorage';

export const authenticate = function*() {
  try {
    const authentication = yield call(getTokenFromSessionStorage);
    yield put(receiveToken(authentication));
    yield* redirect(authentication);
  } catch (error) {
    yield put(receiveTokenFromStorageError(error));
  }
};

export const redirect = function*(authentication) {
  if (!authentication) {
    yield put(push('/login'));
  }
};

export const authenticateSaga = [
  takeEvery(REQUEST_TOKEN_FROM_STORAGE, authenticate)
];
