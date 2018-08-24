import { call, put, takeEvery } from 'redux-saga/effects';
import { push } from 'connected-react-router';
import {
  REQUEST_TOKEN_FROM_STORAGE,
  receiveTokenFromStorageError
} from '../actions/getTokenFromSessionStorageActions';
import { receiveToken } from '../actions/getTokenActions';
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
  yield put(push(authentication ? '/leave' : '/login'));
};

export const authenticateSaga = [
  takeEvery(REQUEST_TOKEN_FROM_STORAGE, authenticate)
];
