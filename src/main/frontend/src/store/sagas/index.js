import { randomUserApiSaga } from './randomUserApiSaga';
import { getTokenSaga } from './getTokenSaga';
import { all } from 'redux-saga/effects';
import { authenticateSaga } from './getTokenFromSessioStorageSaga';

export default function* saga() {
  yield all([...randomUserApiSaga, ...getTokenSaga, ...authenticateSaga]);
}
