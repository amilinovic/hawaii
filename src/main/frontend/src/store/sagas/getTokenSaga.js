import { call, put, all, takeEvery } from 'redux-saga/effects';
import {
  REQUEST_TOKEN,
  receiveAuthorization,
  receiveAuthorizationError
} from '../actions/GetTokenActions';
import { tokenRequest } from '../services/getTokenRequest';

export const getTokenSagaRequest = function*(action) {
  try {
    const responseGoogle = yield call(tokenRequest, action.payload.accessToken);
    yield put(receiveAuthorization(responseGoogle));
  } catch (error) {
    yield put(receiveAuthorizationError(error));
  }
};

export default function* getTokenSaga() {
  yield all([takeEvery(REQUEST_TOKEN, getTokenSagaRequest)]);
}
