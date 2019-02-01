import { getTokenSaga } from './getTokenSaga';
import { all } from 'redux-saga/effects';
import { authenticateSaga } from './getTokenFromSessionStorageSaga';
import { teamsSaga } from './teamsSaga';
import { userSaga } from './userSaga';
import { publicHolidaysSaga } from './publicHolidaySaga';
import { employeesSaga } from './employeesSaga';
import { personalDaysSaga } from './personalDays';
import { leaveTypesSaga } from './leaveTypesSaga';

export default function* saga() {
  yield all([
    ...getTokenSaga,
    ...authenticateSaga,
    ...teamsSaga,
    ...userSaga,
    ...publicHolidaysSaga,
    ...personalDaysSaga,
    ...employeesSaga,
    ...leaveTypesSaga
  ]);
}
