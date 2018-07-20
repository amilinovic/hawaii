import { call, put, all, takeEvery } from 'redux-saga/effects';
import { push } from 'react-router-redux';
import store from '../Store';
import {
  REQUEST_AUTHENTICATION,
  receiveAuthentication,
  receiveAuthenticationError
} from '../actions/AuthenticateActions';
import { authenticateRequest } from '../services/authenticateRequest';

export const authenticateSagaRequest = function*() {
  try {
    const authentication = yield call(authenticateRequest);
    yield put(receiveAuthentication(authentication));
    authentication === false
      ? store.dispatch(push('/login'))
      : store.dispatch(push('/leave'));
  } catch (error) {
    yield put(receiveAuthenticationError(error));
  }
};

export default function* authenticateSaga() {
  yield all([takeEvery(REQUEST_AUTHENTICATION, authenticateSagaRequest)]);
}
