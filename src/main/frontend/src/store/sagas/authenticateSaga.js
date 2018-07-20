import { call, put, all, takeEvery } from 'redux-saga/effects';
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
  } catch (error) {
    yield put(receiveAuthenticationError(error));
  }
};

export default function* authenticateSaga() {
  yield all([takeEvery(REQUEST_AUTHENTICATION, authenticateSagaRequest)]);
}
