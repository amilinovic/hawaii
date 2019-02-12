import { all } from 'redux-saga/effects';
import { employeesSaga } from './employeesSaga';
import { authenticateSaga } from './getTokenFromSessionStorageSaga';
import { getTokenSaga } from './getTokenSaga';
import { teamSaga } from './teamSaga';
import { teamsSaga } from './teamsSaga';
import { userSaga } from './userSaga';

export default function* saga() {
  yield all([
    ...getTokenSaga,
    ...authenticateSaga,
    ...teamsSaga,
    ...teamSaga,
    ...userSaga,
    ...employeesSaga
  ]);
}
