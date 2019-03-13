import { all } from 'redux-saga/effects';
import { allowanceSaga } from './allowanceSaga';
import { employeeSaga } from './employeeSaga';
import { employeesSaga } from './employeesSaga';
import { employeesSearchSaga } from './employeesSearchSaga';
import { authenticateSaga } from './getTokenFromSessionStorageSaga';
import { getTokenSaga } from './getTokenSaga';
import { leaveProfileSaga } from './leaveProfileSaga';
import { leaveProfilesSaga } from './leaveProfilesSaga';
import { leaveTypeSaga } from './leaveTypeSaga';
import { leaveTypesSaga } from './leaveTypesSaga';
import { personalDaysSaga } from './personalDaysSaga';
import { publicHolidaySaga } from './publicHolidaySaga';
import { publicHolidaysSaga } from './publicHolidaysSaga';
import { requestsSaga } from './requestsSaga';
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
    ...leaveTypeSaga,
    ...personalDaysSaga,
    ...publicHolidaysSaga,
    ...publicHolidaySaga,
    ...leaveProfilesSaga,
    ...leaveProfileSaga,
    ...allowanceSaga,
    ...requestsSaga
  ]);
}
