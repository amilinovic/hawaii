import { all } from 'redux-saga/effects';
import { employeeSaga } from './employeeSaga';
import { employeesSaga } from './employeesSaga';
import { authenticateSaga } from './getTokenFromSessionStorageSaga';
import { getTokenSaga } from './getTokenSaga';
import { leaveTypesSaga } from './leaveTypesSaga';
import { personalDaysSaga } from './personalDays';
import { publicHolidaysSaga } from './publicHolidaySaga';
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
    ...employeesSaga,
    ...employeeSaga,
    ...leaveTypesSaga,
    ...personalDaysSaga,
    ...publicHolidaysSaga
  ]);
}
