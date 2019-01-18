import { call, put, takeLatest } from 'redux-saga/effects';
import { getMyPersonalDaysApi } from '../services/myPersonalDaysApi';
import moment from 'moment';
import { initDate } from '../actions/calendarActions';
import {
  errorReceivingMyPersonalDays,
  receiveMyPersonalDays,
  requestMyPersonalDays
} from '../actions/myPersonalDaysActions';

export const getMyPersonalDays = function*() {
  try {
    const myPersonalDays = yield call(getMyPersonalDaysApi);
    yield put(receiveMyPersonalDays(myPersonalDays));
    const now = moment();
    yield put(
      initDate({
        now: now,
        months: moment.months(),
        year: now.year(),
        currentMonth: moment.months(now.month()),
        currentDay: moment().date(),
        myPersonalDays: myPersonalDays
      })
    );
  } catch (error) {
    yield put(errorReceivingMyPersonalDays(error));
  }
};

export const myPersonalDaysSaga = [
  takeLatest(requestMyPersonalDays, getMyPersonalDays)
];
