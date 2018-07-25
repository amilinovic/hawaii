import { call, put, all, takeEvery } from 'redux-saga/effects';
import { push } from 'react-router-redux';
import {
  REQUEST_AUTHENTICATION,
  receiveAuthenticationError
} from '../actions/AuthenticateActions';
import { receiveAuthorization } from '../actions/GetTokenActions';
import { authenticateRequest } from '../services/authenticateRequest';

export const authenticateSagaRequest = function*() {
  try {
    const authentication = yield call(authenticateRequest);
    yield put(receiveAuthorization(authentication));
    authentication.request === false
      ? yield put(push('/login'))
      : yield put(push('/leave'));
  } catch (error) {
    yield put(receiveAuthenticationError(error));
  }
};

export default function* authenticateSaga() {
  yield all([takeEvery(REQUEST_AUTHENTICATION, authenticateSagaRequest)]);
}
