import { call, put, all, takeEvery } from 'redux-saga/effects';
import {
  REQUEST_AUTHORIZATION,
  receiveAuthorization,
  receiveAuthorizationError
} from '../actions/AuthorizationActions';
import { authorizationRequest } from '../services/authorizationRequest';

export const authorizationSagaRequest = function*(action) {
  try {
    const responseGoogle = yield call(
      authorizationRequest,
      action.payload.accessToken
    );
    yield put(receiveAuthorization(responseGoogle));
  } catch (error) {
    yield put(receiveAuthorizationError(error));
  }
};

export default function* authorizationSaga() {
  yield all([takeEvery(REQUEST_AUTHORIZATION, authorizationSagaRequest)]);
}
