import { call, put, takeEvery } from 'redux-saga/effects';
import { push } from 'connected-react-router';
import {
  REQUEST_TOKEN_FROM_STORAGE,
  receiveTokenFromStorageError
} from '../actions/GetTokenFromSessionStorageActions';
import { receiveToken } from '../actions/GetTokenActions';
import { getTokenFromSessionStorage } from '../services/getTokenFromSessionStorage';

export const authenticate = function*() {
  try {
    const authentication = yield call(getTokenFromSessionStorage);
    console.log(authentication);
    yield put(receiveToken(authentication));
    const redirect = authentication ? '/leave' : '/login';
    yield put(push(redirect));
  } catch (error) {
    yield put(receiveTokenFromStorageError(error));
  }
};

export const authenticateSaga = [
  takeEvery(REQUEST_TOKEN_FROM_STORAGE, authenticate)
];
