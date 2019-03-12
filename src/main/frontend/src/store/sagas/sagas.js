import { all } from 'redux-saga/effects';
import { employeeSaga } from './employeeSaga';
import { employeesSaga } from './employeesSaga';
import { employeesSearchSaga } from './employeesSearchSaga';
import { authenticateSaga } from './getTokenFromSessionStorageSaga';
import { getTokenSaga } from './getTokenSaga';
import { leaveProfilesSaga } from './leaveProfilesSaga';
import { leaveTypesSaga } from './leaveTypesSaga';
import { personalDaysSaga } from './personalDaysSaga';
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
    ...employeesSearchSaga,
    ...leaveTypesSaga,
    ...personalDaysSaga,
    ...publicHolidaysSaga,
    ...leaveProfilesSaga
  ]);
}
