import { call, put, takeLatest } from 'redux-saga/effects';
import {
  receivePublicHolidays,
  errorReceivingPublicHolidays,
  requestPublicHolidays
} from '../actions/publicHolidayActions';
import { getPublicHolidaysApi } from '../services/publicHolidaysApi';
import moment from 'moment';
import { initDate } from '../actions/calendarActions';

export const getPublicHolidays = function*() {
  try {
    const publicHolidays = yield call(getPublicHolidaysApi);
    yield put(receivePublicHolidays(publicHolidays));
    const now = moment();
    yield put(
      initDate({
        now: now,
        months: moment.months(),
        year: now.year(),
        currentMonth: moment.months(now.month()),
        currentDay: moment().date(),
        publicHolidays: publicHolidays
      })
    );
  } catch (error) {
    yield put(errorReceivingPublicHolidays(error));
  }
};

export const publicHolidaysSaga = [
  takeLatest(requestPublicHolidays, getPublicHolidays)
];
