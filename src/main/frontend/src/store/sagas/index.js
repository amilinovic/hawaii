import { randomUserApiSaga } from './randomUserApiSaga';
import { getTokenSaga } from './getTokenSaga';
import { all } from 'redux-saga/effects';
import { authenticateSaga } from './getTokenFromSessionStorageSaga';
import { teamsSaga } from './teamsSaga';
import { userSaga } from './userSaga';

export default function* saga() {
  yield all([
    ...randomUserApiSaga,
    ...getTokenSaga,
    ...authenticateSaga,
    ...teamsSaga,
    ...userSaga
  ]);
}
